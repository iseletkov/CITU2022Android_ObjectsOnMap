package ru.permkrai.it.android.objectsonmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class CActivityMain : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.d("Приложение", "Активность открывается")
        setContentView(R.layout.activity_main)

        val editTextInput1 : EditText = findViewById(R.id.editTextInput1)
        val editTextInput2 : EditText = findViewById(R.id.editTextInput2)
        val textViewOutput : TextView =findViewById(R.id.textViewOutput)

        val buttonPlus : Button = findViewById(R.id.buttonPlus)
        buttonPlus.setOnClickListener {
            //Здесь нужно обработать исключения
            val v1 = editTextInput1.text.toString().toDouble()
            val v2 = editTextInput2.text.toString().toDouble()
            textViewOutput.text =  "${v1+v2}"
        }



    }

}