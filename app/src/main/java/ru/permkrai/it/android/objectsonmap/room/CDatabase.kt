package ru.permkrai.it.android.objectsonmap.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.permkrai.it.android.objectsonmap.dao.IDAOObjects
import ru.permkrai.it.android.objectsonmap.model.CComment
import ru.permkrai.it.android.objectsonmap.model.CObject

@Database(entities = [
        CObject::class,
        CComment::class
    ],
    version = 1,
    exportSchema = false)
public abstract class CDatabase             : RoomDatabase() {

    abstract fun daoObjects()               : IDAOObjects


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE                : CDatabase?
                                            = null

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
                    "database.db"
                )
                    .build()
                INSTANCE                    = instance
                // return instance
                instance
            }
        }
    }
}