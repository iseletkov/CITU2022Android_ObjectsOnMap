package ru.permkrai.it.android.objectsonmap.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.permkrai.it.android.objectsonmap.R


class CActivityLogin : AppCompatActivity() {
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val tests = data?.getStringExtra("MY_KEY_3")
                val x = 123
                //doSomeOperations()
            }
        }
    }
    fun onButtonClick(view : View)
    {
        //Вызов активности без отслеживания результата
        //startActivity()

        //Устаревший способ с отслеживанием результата
        //startActivityForResult(intent, 1)

        //Актуальный способ
        val intent = Intent(this, CActivityCalculator::class.java)
        intent.putExtra("MY_KEY_STRING", "Это тестовая строка")
        intent.putExtra("MY_KEY_DOUBLE", 123.456)
        resultLauncher.launch(intent)
    }

}








