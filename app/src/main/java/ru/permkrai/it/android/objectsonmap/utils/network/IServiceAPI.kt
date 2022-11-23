package ru.permkrai.it.android.objectsonmap.utils.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.utils.jsonadapters.CUUIDAdapter

/********************************************************************************************************
 * Объекты, отвечающие за работу с HTTP REST API.                                                       *
 * Весь этот файл должен быть реализован средствами инструмента внедрения зависимостей.                 *
 * @author Селетков И.П. 2022 1123.                                                                     *
 *******************************************************************************************************/
//Адрес сервера в API
private const val BASE_URL =
    "http://192.168.1.101:8080"

//Объект для преобразования текста в формате json  в объекты Kotlin и обратно.
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(CUUIDAdapter()) //Этот класс преобразует в json объекты типа UUID
    .build()

//Объект для отправки запросов на сервер,ожидания и обработки ответов.
private val retrofit = Retrofit.Builder()
    //.addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

//Интерфейс, осуществляющий привязку методов Kotlin к запросам HTTP REST API.
interface IServiceAPI{
    @GET("/objects")
    suspend fun getObjectsOnMap() : List<CObject>
}
//Вспомогательный объект, отвечающий за создание экземпляра интерфейса IServiceAPI
object ServiceAPIFactory {
    val serviceAPIObjects : IServiceAPI by lazy {
        retrofit.create(IServiceAPI::class.java)
    }
}