package ru.permkrai.it.android.objectsonmap.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.permkrai.it.android.objectsonmap.model.CObject

@Dao
interface IDAOObjects {
    @Query("SELECT * FROM objects")
    fun getAll(): Flow<List<CObject>>

    @Insert
    suspend fun insertAll(vararg objects: CObject)

    @Delete
    suspend fun delete(`object`: CObject)
}