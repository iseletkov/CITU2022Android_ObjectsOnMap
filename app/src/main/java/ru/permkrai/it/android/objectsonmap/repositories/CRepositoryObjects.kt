package ru.permkrai.it.android.objectsonmap.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import ru.permkrai.it.android.objectsonmap.dao.IDAOObjects
import ru.permkrai.it.android.objectsonmap.model.CObject

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CRepositoryObjects(
    private val daoObjects                  : IDAOObjects
    )
{

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allObjects                          : Flow<List<CObject>>
                                            = daoObjects.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(
        `object`                            : CObject
    ) {
        daoObjects.insertAll(`object`)
    }
    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(
        `object`                            : CObject
    ) {
        daoObjects.delete(`object`)
    }

}