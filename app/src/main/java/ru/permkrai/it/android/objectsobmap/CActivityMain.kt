package ru.permkrai.it.android.objectsobmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class CActivityMain : AppCompatActivity() {
    val obj = CObject("Это имя", "Это описание")

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val button : Button = findViewById(R.id.button1);
        val editTextInput : EditText = findViewById(R.id.editTextInput)
        val textViewOutput : TextView =findViewById(R.id.textViewOutput)
        button.setOnClickListener {
        obj.comments.add(editTextInput.text.toString())

        //Toast.makeText(applicationContext, "Результат выражения: ${obj.comments.size}", Toast.LENGTH_SHORT).show()
            textViewOutput.text = ""+obj.comments.size
        }

    }
}