package ru.permkrai.it.android.objectsonmap.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import ru.permkrai.it.android.objectsonmap.dao.IDAOObjects
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.utils.network.IServiceAPI
import java.util.UUID

/********************************************************************************************************
 * Репозиторий для работы с данными об объектах.                                                        *
 * @author Селетков И.П. 2022 1116.                                                                     *
 *******************************************************************************************************/
class CRepositoryObjects(
    private val daoObjects                  : IDAOObjects,
    private val serviceAPI                  : IServiceAPI
)
{
    /****************************************************************************************************
     * Получение списка всех элементов.                                                                 *
     ***************************************************************************************************/
    fun getAll()                            : Flow<List<CObject>>
    {
        return daoObjects.getAllFlow()
    }
    suspend fun updateFromServer()          : String
    {
        return try{
            serviceAPI.getObjectsOnMap()
                .forEach { objectFromServer ->
                    daoObjects.getById(objectFromServer.id)?.let {
                        daoObjects.update(objectFromServer)
                    } ?: run {
                        daoObjects.insertAll(objectFromServer)
                    }
                }
            "Данные обновлены с сервера"
        }
        catch(e : Exception) {
            Log.d("OBJECTS_ON_MAP", "Не удалось загрузить данные с сервера!",e)
            "Не удалось загрузить данные с сервера!"
        }

    }

    /****************************************************************************************************
     * Получение элемента по идентификатору.                                                            *
     * @param id - идентификатор элемента для поиска.                                                   *
     * @return объект с идентификатором id или ??? (упадёт) в случае отсутствия.                        *
     ***************************************************************************************************/
    fun getById(
        id                                  : UUID
    )                                       = daoObjects.getByIdFlow(id)

    /****************************************************************************************************
     * Сохранение нового элемента в БД.                                                                 *
     * @param item - объект для сохранения.                                                             *
     ***************************************************************************************************/
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(
        item                                : CObject
    ) {
        daoObjects.insertAll(item)
    }
    /****************************************************************************************************
     * Удаление существующего объекта.                                                                  *
     * @param item - объект для удаления.                                                               *
     ***************************************************************************************************/
    @WorkerThread
    suspend fun delete(
        item                            : CObject
    ) {
        daoObjects.delete(item)
    }
    /****************************************************************************************************
     * Обновление существующего объекта.                                                                *
     * @param item - объект для обновления.                                                             *
     ***************************************************************************************************/
    suspend fun update(
        item                            : CObject
    ) {
        daoObjects.update(item)
    }

}