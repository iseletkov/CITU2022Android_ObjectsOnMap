package ru.permkrai.it.android.objectsonmap.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.permkrai.it.android.objectsonmap.dao.IDAOObjects
import ru.permkrai.it.android.objectsonmap.model.CComment
import ru.permkrai.it.android.objectsonmap.model.CObject

/********************************************************************************************************
 * Класс для работы с БД.                                                                               *
 * @author Селетков И.П. 2022 1116                                                                      *
 *******************************************************************************************************/
@Database(
    //Список сущностей, которые будут храниться в БД.
    entities                                = [
        CObject::class,
        CComment::class
    ],
    version                                 = 1,
    exportSchema                            = false
)
abstract class CDatabase                    : RoomDatabase() {
    /****************************************************************************************************
     * Класс для доступа к данным объектов в БД.                                                        *
     ***************************************************************************************************/
    abstract fun daoObjects()               : IDAOObjects


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE                : CDatabase?
                                            = null

        /************************************************************************************************
         * Статический метод для создания единого на всю программу экземпляра класса CDatabase.         *
         ***********************************************************************************************/
        fun getDatabase(
            context                         : Context
        )                                   : CDatabase
        {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE                 ?: synchronized(this) {
                val instance                = Room.databaseBuilder(
                    context.applicationContext,
                    CDatabase::class.java,
                    "database.db" //Название файла в файловой системе.
                )
                    .build()
                INSTANCE                    = instance
                // return instance
                instance
            }
        }
    }
}