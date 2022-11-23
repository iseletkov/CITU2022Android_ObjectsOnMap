package ru.permkrai.it.android.objectsonmap.utils.jsonadapters

import android.util.Log
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

/********************************************************************************************************
 * Класс отвечает за преобразование объектов UUID в строку json и обратно.                              *
 * @author Селетков И.П. 2022 1123.                                                                     *
 *******************************************************************************************************/
class CUUIDAdapter {
    @FromJson
    fun fromJson(
        uuid                                : String
    )                                       : UUID
    {
        return try{
            UUID.fromString(uuid)
        }
        catch(e : Exception)
        {
            //TODO Лог
            Log.d("OBJECTS_ON_MAP", "Не удалось преобразовать json в UUID!", e)
            UUID.randomUUID()
        }
    }

    @ToJson
    fun toJson(
        value                               : UUID
    )                                       : String
                                            = value.toString()
}
