package ru.permkrai.it.android.objectsonmap.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import com.google.android.material.textfield.TextInputLayout
import ru.permkrai.it.android.objectsonmap.R


class CActivityCalculator : AppCompatActivity() {
    private lateinit var editTextInput1 : TextInputLayout
    private lateinit var editTextInput2 : TextInputLayout
    private lateinit var textViewOutput : TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.d("Приложение", "Активность открывается")
        setContentView(R.layout.activity_main)

        editTextInput1 = findViewById(R.id.input1)
        editTextInput2 = findViewById(R.id.input2)
        textViewOutput = findViewById(R.id.textViewOutput)

        val buttonPlus : Button = findViewById(R.id.buttonPlus)
        buttonPlus.setOnClickListener {
            //Здесь нужно обработать исключения
            val v1 = editTextInput1.editText?.text.toString().toDouble()
            val v2 = editTextInput2.editText?.text.toString().toDouble()
            textViewOutput.text = String.format(getString(R.string.Output), v1+v2)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_activity_calculator, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.miAbout -> {
                Toast.makeText(this, "Нажата кнопка \"О приложении\"", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.miAdd -> {
                //Здесь нужно обработать исключения
                val v1 = editTextInput1.editText?.text.toString().toDouble()
                val v2 = editTextInput2.editText?.text.toString().toDouble()
                textViewOutput.text = String.format(getString(R.string.Output), v1+v2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}