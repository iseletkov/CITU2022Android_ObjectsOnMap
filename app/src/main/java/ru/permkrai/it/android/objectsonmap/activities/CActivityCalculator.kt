package ru.permkrai.it.android.objectsonmap.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import ru.permkrai.it.android.objectsonmap.R
import ru.permkrai.it.android.objectsonmap.databinding.ActivityCalculatorBinding



class CActivityCalculator : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding

//    val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
//        when {
//            granted -> {
//                requestCamera.launch() // доступ к камере разрешен, открываем камеру
//            }
//            !shouldShowRequestPermissionRationale(CAMERA) -> {
//                // доступ к камере запрещен, пользователь поставил галочку Don't ask again.
//            }
//            else -> {
//                // доступ к камере запрещен
//            }
//        }
//    }
//    val requestCamera = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//        // используем bitmap
//    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.d("Приложение", "Активность открывается")
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val tests = intent.extras?.getString("MY_KEY_STRING")
//        val testd = intent.extras?.getDouble("MY_KEY_DOUBLE")

        intent.extras?.let{
            val tests = it.getString("MY_KEY_STRING")
            val testd = it.getDouble("MY_KEY_DOUBLE")
        }?:run{
            Toast.makeText(this, "Параметр недоступны!", Toast.LENGTH_SHORT).show()
        }

//        if (intent.extras==null)
//        {
//            Toast.makeText(this, "Параметр недоступны!", Toast.LENGTH_SHORT).show()
//        }
//        else
//        {
//            val tests = intent.extras!!.getString("MY_KEY_STRING")
//            val testd = intent.extras!!.getDouble("MY_KEY_DOUBLE")
//            val x = 123
//        }

        binding.buttonPlus.setOnClickListener {
            plus()
        }

        onBackPressedDispatcher.addCallback(
            this /* lifecycle owner */,
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    // Back is pressed... Finishing the activity
                    val myIntent = Intent()
                    myIntent.putExtra("MY_KEY_4", "Это строка, которая возвращается при нажатии кнопки назад")
                    setResult(RESULT_OK, myIntent)
                    finish()
                }
            }
        )
    }
    private fun plus()
    {
        //Здесь нужно обработать исключения
        val v1 = binding.input1.editText?.text.toString().toDouble()
        val v2 = binding.input2.editText?.text.toString().toDouble()
        binding.textViewOutput.text = String.format(getString(R.string.Output), v1+v2)
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
//            R.id.miPhoto ->
//            {
//                if (shouldShowRequestPermissionRationale(CAMERA)) {
//                    // объясняем пользователю, почему нам необходимо данное разрешение
//                } else {
//                    requestPermission.launch(CAMERA)
//                }
//            }
            R.id.miClose -> {
                val myIntent = Intent()
                myIntent.putExtra("MY_KEY_3", "Это строка, которая возвращается обратно")
                setResult(RESULT_OK, myIntent)
                finish()
                true
            }
            R.id.miAdd -> {
                plus()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
        //Устаревший вариант
//    override fun onBackPressed() {
//        super.onBackPressed()
//    }


}