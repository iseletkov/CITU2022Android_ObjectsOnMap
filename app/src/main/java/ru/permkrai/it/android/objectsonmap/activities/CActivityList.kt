package ru.permkrai.it.android.objectsonmap.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.permkrai.it.android.objectsonmap.R
import ru.permkrai.it.android.objectsonmap.adapters.CRecyclerViewAdapterObjects
import ru.permkrai.it.android.objectsonmap.databinding.ActivityListBinding
import ru.permkrai.it.android.objectsonmap.model.CObject

/********************************************************************************************************
 * Активность с отображением списка объектов на карте.                                                  *
 *******************************************************************************************************/
class CActivityList                         : AppCompatActivity()
{
    private lateinit var resultLauncherObjectEdit : ActivityResultLauncher<Intent>
    private lateinit var resultLauncherObjectAdd : ActivityResultLauncher<Intent>

    //Объект класса, содержащий сылки на управляющие графические элементы интерфейса пользователя.
    private lateinit var binding            : ActivityListBinding

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
    }
}