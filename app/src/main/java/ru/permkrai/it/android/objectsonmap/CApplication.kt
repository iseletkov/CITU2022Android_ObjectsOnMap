package ru.permkrai.it.android.objectsonmap

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import ru.permkrai.it.android.objectsonmap.repositories.CRepositoryObjects
import ru.permkrai.it.android.objectsonmap.utils.network.ServiceAPIFactory
import ru.permkrai.it.android.objectsonmap.utils.room.CDatabase


/********************************************************************************************************
 * Основной класс программы.                                                                            *
 *******************************************************************************************************/
class CApplication                          : Application()
{
    private val database by lazy { CDatabase.getDatabase(this) }
    val repositoryObjects by lazy {
        CRepositoryObjects(
            database.daoObjects(),
            ServiceAPIFactory.serviceAPIObjects
        )
    }
    override fun onCreate() {
        super.onCreate()

    }
}