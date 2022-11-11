package ru.permkrai.it.android.objectsonmap.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import ru.permkrai.it.android.objectsonmap.R
import ru.permkrai.it.android.objectsonmap.databinding.ActivityLoginBinding

/********************************************************************************************************
 * Активность для ввода учётных данных.                                                                 *
 *******************************************************************************************************/

class CActivityLogin : AppCompatActivity() {
    //Ссылка а объект для работы с настройками приложения.
    private lateinit var pref               : SharedPreferences

    private lateinit var binding            : ActivityLoginBinding

    /****************************************************************************************************
     * Обработка события создания объекта активности.                                                   *
     ***************************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding                             = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Получаем ссылку на объект, ассоциируем его с файлом.
        pref                                = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        //Чтение ранее сохранённого имени пользователя.
        val userName = pref.getString(getString(R.string.KEY_USER_NAME), "")
        //Если пользователь уже зарегистрировался.
        if (userName!="")
        {
            //Вызов активности со списком.
            val intent = Intent(this, CActivityList::class.java)
            startActivity(intent)
            //Закрываем текущую активность, чтобы на неё нельзя было вернуться кнопкой "назад".
            finish()
            return
        }
        /************************************************************************************************
         * Обработка клика на кнопку "войти".                                                           *
         ***********************************************************************************************/
        binding.buttonLogin.setOnClickListener{
            onButtonLoginClick()
        }

    }
    /****************************************************************************************************
     * Обработка клика на кнопку "войти".                                                               *
     ***************************************************************************************************/
    private fun onButtonLoginClick()
    {
        //Проверяем учётные данные пользователя.
        val userName                        = checkLogin("${binding.inputLogin.editText?.text ?: ""}",
            "${binding.inputPassword.editText?.text ?: ""}"
        )
        //Если данные некорректны, выводим сообщение,
        //завершаем обработку.
        if (userName=="")
        {
            Toast.makeText(this, "Данные не верны!", Toast.LENGTH_SHORT).show()
            return
        }
        //Если данные верные

        //Сохраняем в файл с настройками приложения учётную запись пользователя.
        //Пароль не храним.
        //Если надо хранить, то только в зашифрованном виде.
        with (pref.edit()) {
            putString(getString(R.string.KEY_USER_NAME), userName)
            apply()
        }

        //Вызов активности со списком.
        val intent                          = Intent(this, CActivityList::class.java)
        startActivity(intent)
        //Закрываем данную активность,
        //чтобы в неё нельзя было вернуться нажатием кнопки "назад".
        finish()
    }
    /****************************************************************************************************
     * Проверка учётных данных.                                                                         *
     * @param login - наименование учётной записи или электронная почта пользователя.                   *
     * @param password - пароль пользователя.                                                           *
     * @return имя учёт ной записи в случае корректности или пустую строку в случае проблемы.           *
     ***************************************************************************************************/
    private fun checkLogin(
        login                               : String,
        password                            : String
    )                                       : String
    {
        //Здесь должен быть запрос на сервер.
        //Пароль должен быть зашифрован,
        //протокол должен быть httpS.
        if (login=="test@gmail.com" && password=="test123")
            return "test"
        return ""
    }
}








