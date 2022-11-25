package ru.permkrai.it.android.objectsonmap.activities

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.permkrai.it.android.objectsonmap.CApplication
import ru.permkrai.it.android.objectsonmap.R
import ru.permkrai.it.android.objectsonmap.adapters.CRecyclerViewAdapterObjects
import ru.permkrai.it.android.objectsonmap.databinding.ActivityListBinding
import ru.permkrai.it.android.objectsonmap.viewmodels.CViewModelActivityList
import ru.permkrai.it.android.objectsonmap.viewmodels.CViewModelFactory

/********************************************************************************************************
 * Активность с отображением списка объектов на карте.                                                  *
 *******************************************************************************************************/
class CActivityList                         : AppCompatActivity()
{
    private lateinit var resultLauncherPermission
                                            : ActivityResultLauncher<Array<String>>

    //Объект класса, содержащий сылки на управляющие графические элементы интерфейса пользователя.
    private lateinit var binding            : ActivityListBinding

    private var test = 0

    //Ссылка а объект для работы с настройками приложения.
    private lateinit var pref               : SharedPreferences

    //Получение ссылки на экземляр класса CViewModelActivityList
    private val viewModel                   : CViewModelActivityList by viewModels {
        CViewModelFactory((application as CApplication).repositoryObjects)
    }

    /****************************************************************************************************
     * Обработка события создания объекта активности.                                                   *
     ***************************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Связываем код актиности с файлом, описывающим внешний вид активности.
        binding                             = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Получаем ссылку на объект настроек, ассоциируем его с файлом.
        pref                                = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        /************************************************************************************************
         * Обработка изменений списка объектов в базе данных.                                           *
         ***********************************************************************************************/
        lifecycleScope.launch { //Запускаем короутину в пределах жизненного цикла активности.
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // This happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                //Получаем информацию из потока списков объектов.
                viewModel.allObjects.collect { newItems ->
                    //Передаём очереной актуализированный список объектов в адаптер для отображения.
                    (binding.rvObjects.adapter as CRecyclerViewAdapterObjects?)?.submitList(newItems)
                }
            }
        }
        //Способ расположения элементов - списком
        binding.rvObjects.layoutManager     = LinearLayoutManager(this)
        //Управление отображаемыми элементами - наш класс-адаптер.
        binding.rvObjects.adapter           = CRecyclerViewAdapterObjects(
            //Обработчик клика по элементу.
            { item ->
                //Вызов активности с информацией по объекту, передача туда идентификатора.
                val intent                  = Intent(this, CActivityObjectInfo::class.java)
                intent.putExtra(getString(R.string.KEY_OBJECT_ID), item.id.toString())
                startActivity(intent)
            },
            //Обработчик клика на кнопку "удалить" элемента.
            { item ->
                viewModel.delete(item)
            }
        )


        /************************************************************************************************
         * Обработка клика на плавующую кнопку.                                                         *
         ***********************************************************************************************/
        binding.fab.setOnClickListener {
            //Вызов активности с информацией по объекту, передача туда параметров.
            val intent                      = Intent(this, CActivityObjectInfo::class.java)
            startActivity(intent)
        }

        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog. Save the return value, an instance of
        // ActivityResultLauncher. You can use either a val, as shown in this snippet,
        // or a lateinit var in your onAttach() or onCreate() method.
        resultLauncherPermission            =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { map: Map<String, Boolean> ->
                if (map[Manifest.permission.ACCESS_FINE_LOCATION]==true) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    test = 1
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    test = 2
                }
            }
        //Вызываем обновление объектов с сервера.
        viewModel.getObjectsFromServer()
        /************************************************************************************************
         * Обработка изменений статуса загрузки данных с сервера.                                       *
         ***********************************************************************************************/
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.status.collect { newStatus ->
                    if (newStatus.isEmpty())
                        return@collect //Пропускаем пустой начальный статус
                    //Показываем статусное сообщение.
                    Toast.makeText(
                        this@CActivityList,
                        newStatus,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        checkAndRequestPermissions()
    }
    /****************************************************************************************************
     * Проверка наличия и запрос необходимых разрешений.                                                *
     ***************************************************************************************************/
    private fun checkAndRequestPermissions()
    {
        val allPermissions                  = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        val permissionsToAsk                = allPermissions
            .filter {
                return@filter ContextCompat.checkSelfPermission(
                this,
                    it
                ) == PackageManager.PERMISSION_DENIED
            }
        if (permissionsToAsk.isNotEmpty())
            resultLauncherPermission.launch(
                permissionsToAsk.toTypedArray()
            )
    }
    /****************************************************************************************************
     * Привязка файла с описанием структуры меню к данной активности при создании активности.           *
     ***************************************************************************************************/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater          = menuInflater
        inflater.inflate(R.menu.menu_activity_list, menu)
        return true
    }
    /****************************************************************************************************
     * Обработка нажатия на элементы меню.                                                              *
     ***************************************************************************************************/
    override fun onOptionsItemSelected(
        item                                : MenuItem
    )                                       : Boolean
    {
        return when (item.itemId) {
            R.id.miMapGoogle -> {
                switchToMapGoogle()
                true
            }
            R.id.miMapYandex -> {
                switchToMapYandex()
                true
            }
            R.id.miLogout -> {
                doLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    /****************************************************************************************************
     * Обработка нажатия кнопки "Выход из учётной записи" в меню.                                       *
     ***************************************************************************************************/
    private fun doLogout()
    {
        //Сохраняем в файл с настройками приложения факт отсутствия учётной записи.
        with (pref.edit()) {
            putString(getString(R.string.KEY_USER_NAME), "")
            apply()
        }
        //Закрываем данную активность.
        finish()
        //Опционально можем вызвать активность ввода учётных данных.
        val intent                  = Intent(this, CActivityLogin::class.java)
        startActivity(intent)
    }
    private fun switchToMapGoogle()
    {
        val intent                  = Intent(this, CActivityMapGoogle::class.java)
        startActivity(intent)
    }
    private fun switchToMapYandex()
    {
        val intent                  = Intent(this, CActivityMapYandex::class.java)
        startActivity(intent)
    }
}