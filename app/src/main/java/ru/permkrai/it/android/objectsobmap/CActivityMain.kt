package ru.permkrai.it.android.objectsobmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class CActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val button : Button = findViewById(R.id.button1);
        button.setOnClickListener {
            Toast.makeText(applicationContext, "Привет!", Toast.LENGTH_SHORT).show()
        }
    }
}