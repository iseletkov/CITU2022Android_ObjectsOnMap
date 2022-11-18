package ru.permkrai.it.android.objectsonmap

import android.app.Application
import ru.permkrai.it.android.objectsonmap.repositories.CRepositoryObjects
import ru.permkrai.it.android.objectsonmap.room.CDatabase

/********************************************************************************************************
 * Основной класс программы.                                                                            *
 *******************************************************************************************************/
class CApplication                          : Application()
{
    private val database by lazy { CDatabase.getDatabase(this) }
    val repositoryObjects by lazy { CRepositoryObjects(database.daoObjects()) }
}