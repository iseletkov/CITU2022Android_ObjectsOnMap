package ru.permkrai.it.android.objectsonmap.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import ru.permkrai.it.android.objectsonmap.R
import ru.permkrai.it.android.objectsonmap.databinding.ActivityListBinding
import ru.permkrai.it.android.objectsonmap.databinding.ActivityObjectInfoBinding
/********************************************************************************************************
 * Активность с отображением информации по одному объекту.                                              *
 *******************************************************************************************************/
class CActivityObjectInfo                   : AppCompatActivity()
{
    private lateinit var binding            : ActivityObjectInfoBinding
    //Порядковый номер редактируемого элемента в общем списке.
    private var index                       : Int
                                            = -1 //Если какие-то проблемы, то -1

    /****************************************************************************************************
     * Обработка события создания объекта активности.                                                   *
     ***************************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Связь объектов, реализующих внешний вид с объектом активности.
        binding                             = ActivityObjectInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val extr = intent.extras
//        if ( extr==null)
//        {
//            index = -1
//            binding.inputName.editText!!.setText("")
//        }
//        else
//        {
//            index = extr.getInt("KEY_INDEX")
//            //Вывод наименования объекта на экран.
//            binding.inputName.editText!!.setText(intent.extras!!.getString("KEY_OBJECT_NAME")?: "")
//        }

        //Получение переданных параметров
        intent.extras?.let{
            index = it.getInt("KEY_INDEX")
            //Вывод наименования объекта на экран.
            binding.inputName.editText!!.setText(it.getString("KEY_OBJECT_NAME")?: "")
        }?:run{
            //Toast.makeText(this, "Параметры недоступны!", Toast.LENGTH_SHORT).show()
            index = -1
            binding.inputName.editText!!.setText("")
        }

        /************************************************************************************************
         * Обработка события нажатия кнопки "назад".                                                    *
         ***********************************************************************************************/
        onBackPressedDispatcher.addCallback(
            this /* lifecycle owner */,
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    //Сохраняем введённые параметры и закрываем активность.
                    val myIntent = Intent()
                    myIntent.putExtra("KEY_INDEX", index)
                    //Здесь в явном виде используется преобразование к строке,
                    //потому что без него результат команды считается чем-то более сложным (Parsable, Serializable),
                    //и не отображается методом getStingExtra в родительской форме.
                    myIntent.putExtra("KEY_OBJECT_NAME", "${binding.inputName.editText?.text ?: ""}")
                    setResult(RESULT_OK, myIntent)
                    finish()
                }
            }
        )
    }
}