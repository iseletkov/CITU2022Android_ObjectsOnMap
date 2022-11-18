package ru.permkrai.it.android.objectsonmap.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/********************************************************************************************************
 * Объект, который может быть отмечен на карте.                                                         *
 *******************************************************************************************************/
@Entity(
    tableName                               = "objects"
)
class CObject(
    /****************************************************************************************************
     * Идентификатор.                                                                                   *
     ***************************************************************************************************/
    @PrimaryKey
    var id                                  : UUID
                                            = UUID.randomUUID(),
    /****************************************************************************************************
     * Наименование объекта.                                                                            *
     ***************************************************************************************************/
    @ColumnInfo(
        name                                = "name"
    ) //Название поля в таблице из БД
    var name                                : String
                                            = "",
    /****************************************************************************************************
     * Описание объекта.                                                                                *
     ***************************************************************************************************/
    @ColumnInfo(
        name                                = "description"
    ) //Название поля в таблице из БД
    var description                         : String
                                            = ""
)
{
    /****************************************************************************************************
     * Перевод в строку.                                                                                *
     ***************************************************************************************************/
    override fun toString(): String {
        return "name: $name, description: $description"
    }
}