package ru.permkrai.it.android.objectsonmap.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.permkrai.it.android.objectsonmap.adapters.CRecyclerViewAdapterObjects
import ru.permkrai.it.android.objectsonmap.databinding.ActivityListBinding
import ru.permkrai.it.android.objectsonmap.model.CObject
import java.io.File

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

    /****************************************************************************************************
     * Обработка события создания объекта активности.                                                   *
     ***************************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Связываем код актиности с файлом, описывающим внешний вид активности.
        binding                             = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Тестовый список объектов, которые будт выводится пользователю.
        val items = mutableListOf<CObject>()
        items.add(CObject("Кунгу́рская ледяна́я пеще́ра", "Одна из крупнейших карстовых пещер в Европейской части России, седьмая в мире гипсовая пещера по протяжённости. Протяжённость пещеры по данным на 2021 год составляет около 8153 м, из них около 2 километров оборудовано для посещений туристами."))
        items.add(CObject("Белого́рский Свято-Никола́евский монасты́рь", "Мужской монастырь на Белой горе в Кунгурском районе Пермского края. Относится к Пермской епархии Русской православной церкви. За строгость устава эту обитель некогда называли Уральским Афономм."))
        items.add(CObject("Пермский краеведческий музей","Старейший и крупнейший музей Пермского края. Насчитывает 600 000 единиц хранения и включает более 50 коллекций регионального, российского и мирового значения, в числе объектов музея 22 памятника истории и культуры, из них 16 памятников федерального значения и 6 местного значения."))
        items.add(CObject("Каменный город", "Это массив из песчаника, который за тысячи лет ветер превратил в скопление массивных столбов-останцев. Перед этим над естественной архитектурой потрудилась река, пробившая в скалах арки и расщелины, делающие естественное образование удивительно схожим с рукотворным городом. По аналогии ущелья названы улицами, а отдельные скалы имеют собственные имена. Местные прозвали достопримечательность Чертовым Городищем."))
        items.add(CObject("Хохловка", "Здесь находятся свезенные со всего края 23 объекта деревянного зодчества, расположившиеся на 35 гектарах музея под открытым небом у берега Камы. Внутри самих строений открыты выставки предметов местных ремесел и творчества, восстановлены интерьеры эпох, к которым относятся здания."))
        items.add(CObject("Усьвинские столбы", "Известняковый массив высотой 120 метров протянулся на километры по правому берегу Усьвы. Как туристический объект интересует скалолазов, спелеологов и любителей археологии. Здесь множество пещер и гротов, причем регулярно открываются новые: скала довольно сложна для восхождения и не вся обследована. Отдельная достопримечательность — Чертов Палец, вертикальный скальный выступ высотой 70 метров."))

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
         * Обработка события завершения активности с информацией по объекту в режиме редактирования
         * существующего объекта.                            *
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
                items.add(CObject(name, "test"))
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
        //checkAndRequestPermissions()
        // Индивидуальный раздел памяти для вашего приложения.
        // val file = File(applicationContext.filesDir, "123.txt")

        //Если версия Android <29
        //val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "123.txt")
        //Если версия Android >=29, папка не очень красивая/удобная
//        val file = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "123.txt")
        //Если версия Android >=29, папка удобная
        //https://developer.android.com/training/data-storage/shared/documents-files

        //Запись произвольного тектсового файла.
//        file.createNewFile()
//        val text = listOf("adawd awdaw", "adad aafgsr", "123 4534")
//        file.printWriter().use { out ->
//            text.forEach {
//                out.println(it)
//            }
//        }
        //Чтение произвольного текстового файла
//        val text = file.readLines()
//            .toList()
//        Log.d("TEST", text.joinToString("\n"))

        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
//        with (pref.edit()) {
//            putInt("KEY_INT", 123)
//            putString("KEY_STRING", "adawd ad ada dada wdawd")
//            apply()
//        }

        val text = pref.getString("KEY_STRING", "default value")
        var chisl = pref.getInt("KEY_INT", 9999)
        test = 123
    }
    private fun checkAndRequestPermissions()
    {
        val allPermissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
//        val permissionsToAsk = mutableListOf<String>()
//        for (i in 0..allPermissions.size)
//        {
//            if (ContextCompat.checkSelfPermission(
//                this,
//                    allPermissions[i]
//            ) == PackageManager.PERMISSION_DENIED)
//                permissionsToAsk.add(allPermissions[i])
//        }
        val permissionsToAsk = allPermissions
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
}