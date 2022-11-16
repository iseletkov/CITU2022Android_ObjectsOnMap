package ru.permkrai.it.android.objectsonmap.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.permkrai.it.android.objectsonmap.CApplication
import ru.permkrai.it.android.objectsonmap.R
import ru.permkrai.it.android.objectsonmap.adapters.CRecyclerViewAdapterObjects
import ru.permkrai.it.android.objectsonmap.databinding.ActivityListBinding
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.viewmodels.CViewModelActivityList
import ru.permkrai.it.android.objectsonmap.viewmodels.CViewModelFactory
import java.util.*

/********************************************************************************************************
 * Активность с отображением списка объектов на карте.                                                  *
 *******************************************************************************************************/
class CActivityList                         : AppCompatActivity()
{
    private lateinit var resultLauncherObjectEdit : ActivityResultLauncher<Intent>
    private lateinit var resultLauncherObjectAdd : ActivityResultLauncher<Intent>
    private lateinit var resultLauncherPermission : ActivityResultLauncher<Array<String>>

    //Объект класса, содержащий сылки на управляющие графические элементы интерфейса пользователя.
    private lateinit var binding            : ActivityListBinding

    private var test = 0

    //Ссылка а объект для работы с настройками приложения.
    private lateinit var pref               : SharedPreferences

    private val viewModelActivityList       : CViewModelActivityList by viewModels {
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


        //Тестовый список объектов, которые будут выводится пользователю.
        val items                           = mutableListOf<CObject>()
        items.add(CObject(UUID.randomUUID(), "Кунгу́рская ледяна́я пеще́ра", "Одна из крупнейших карстовых пещер в Европейской части России, седьмая в мире гипсовая пещера по протяжённости. Протяжённость пещеры по данным на 2021 год составляет около 8153 м, из них около 2 километров оборудовано для посещений туристами."))
        items.add(CObject(UUID.randomUUID(),"Белого́рский Свято-Никола́евский монасты́рь", "Мужской монастырь на Белой горе в Кунгурском районе Пермского края. Относится к Пермской епархии Русской православной церкви. За строгость устава эту обитель некогда называли Уральским Афономм."))
        items.add(CObject(UUID.randomUUID(),"Пермский краеведческий музей","Старейший и крупнейший музей Пермского края. Насчитывает 600 000 единиц хранения и включает более 50 коллекций регионального, российского и мирового значения, в числе объектов музея 22 памятника истории и культуры, из них 16 памятников федерального значения и 6 местного значения."))
        items.add(CObject(UUID.randomUUID(),"Каменный город", "Это массив из песчаника, который за тысячи лет ветер превратил в скопление массивных столбов-останцев. Перед этим над естественной архитектурой потрудилась река, пробившая в скалах арки и расщелины, делающие естественное образование удивительно схожим с рукотворным городом. По аналогии ущелья названы улицами, а отдельные скалы имеют собственные имена. Местные прозвали достопримечательность Чертовым Городищем."))
        items.add(CObject(UUID.randomUUID(),"Хохловка", "Здесь находятся свезенные со всего края 23 объекта деревянного зодчества, расположившиеся на 35 гектарах музея под открытым небом у берега Камы. Внутри самих строений открыты выставки предметов местных ремесел и творчества, восстановлены интерьеры эпох, к которым относятся здания."))
        items.add(CObject(UUID.randomUUID(),"Усьвинские столбы", "Известняковый массив высотой 120 метров протянулся на километры по правому берегу Усьвы. Как туристический объект интересует скалолазов, спелеологов и любителей археологии. Здесь множество пещер и гротов, причем регулярно открываются новые: скала довольно сложна для восхождения и не вся обследована. Отдельная достопримечательность — Чертов Палец, вертикальный скальный выступ высотой 70 метров."))

        binding.rvObjects.layoutManager     = LinearLayoutManager(this)
        binding.rvObjects.adapter           = CRecyclerViewAdapterObjects(
            //Список элементов
            items,
            //Обработчик клика по элементу.
            { index, item ->
                //Вызов активности с информацией по объекту, передача туда параметров.
                val intent                  = Intent(this, CActivityObjectInfo::class.java)
                intent.putExtra("KEY_INDEX", index)
                intent.putExtra("KEY_OBJECT_NAME", item.name)
                resultLauncherObjectEdit.launch(intent)
            },
            //Обработчик клика на кнопку "удалить" элемента.
            { index, _ ->
                items.removeAt(index)
                binding.rvObjects.adapter?.notifyItemRemoved(index)
            }
        )

        /************************************************************************************************
         * Обработка события завершения активности с информацией по объекту в режиме редактирования     *
         * существующего объекта.                                                                       *
         ***********************************************************************************************/
        resultLauncherObjectEdit            = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //Получение параметров из дочерней активности
                val data                    : Intent?
                                            = result.data
                val name                    = data?.getStringExtra("KEY_OBJECT_NAME") ?: ""
                val index                   = data?.getIntExtra("KEY_INDEX", -1)?: -1
                //Если какие-то проблемы с данными, выводи сообщение или как-то обрабатываем.
                if (index<0)
                {
                    //TODO Сообщение о проблеме в передаче данных
                }
                else
                {
                    //Если всё нормально,
                    //актуализируем объект в списке данных.
                    items[index].name       = name
                    //Говорим адаптеру списка, что конкретная единица данных обновлена,
                    //нужно повторно её вывести на экран.
                    (binding.rvObjects.adapter as CRecyclerViewAdapterObjects).notifyItemChanged(index)
                }
            }
        }
        /************************************************************************************************
         * Обработка события завершения активности с информацией по объекту в режиме создания нового    *
         * объекта.                                                                                     *
         ***********************************************************************************************/
        resultLauncherObjectAdd             = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //Получение параметров из дочерней активности
                val data                    : Intent?
                                            = result.data
                val name                    = data?.getStringExtra("KEY_OBJECT_NAME") ?: ""

                //Добавляем объект в список данных.
                items.add(CObject(UUID.randomUUID(), name, "test"))
                //Говорим адаптеру списка, что конкретная единица данных добавлена,
                //нужно повторно её вывести на экран.
                (binding.rvObjects.adapter as CRecyclerViewAdapterObjects).notifyItemInserted(items.size-1)

            }
        }
        /************************************************************************************************
         * Обработка клика на плавующую кнопку.                                                         *
         ***********************************************************************************************/
        binding.fab.setOnClickListener {
            //Вызов активности с информацией по объекту, передача туда параметров.
            val intent                      = Intent(this, CActivityObjectInfo::class.java)
            resultLauncherObjectAdd.launch(intent)
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
}