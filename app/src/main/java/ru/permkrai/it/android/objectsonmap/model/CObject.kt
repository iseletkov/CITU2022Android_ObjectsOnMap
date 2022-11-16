package ru.permkrai.it.android.objectsonmap.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "objects")
class CObject(
    @PrimaryKey
    var id                                  : UUID
                                            = UUID.randomUUID(),
    @ColumnInfo(name = "name") //Название поля в таблице из БД
    var name                                : String
                                            = "",
    @ColumnInfo(name = "description") //Название поля в таблице из БД
    var description                         : String
                                            = ""
)
{
//    var comments : MutableList<String> = mutableListOf()

    override fun toString(): String {
        return "name: $name, description: $description"
    }
}