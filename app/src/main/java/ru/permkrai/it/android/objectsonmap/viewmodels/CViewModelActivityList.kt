package ru.permkrai.it.android.objectsonmap.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.utils.network.ServiceAPIFactory
import ru.permkrai.it.android.objectsonmap.repositories.CRepositoryObjects
import java.util.*

/********************************************************************************************************
 * Модель представления (отображаемых данных и состояния) для активности списка объектов.               *
 *******************************************************************************************************/
class CViewModelActivityList(
    private val repositoryObject            : CRepositoryObjects
)                                           : ViewModel()
{
    //Поток со списками всех объектов.
    val allObjects                          : Flow<List<CObject>>
                                            = repositoryObject.getAll()

    //Статус загрузки данных с сервера
    private val _status                     = MutableStateFlow("")
    val status                              : StateFlow<String>
        get()                               = _status

    /************************************************************************************************
    * Загрузка данных с сервера.                                                                 *
    ***********************************************************************************************/
    fun getObjectsFromServer()
    {
        viewModelScope.launch(Dispatchers.IO) {
            //Вызываем метод загрузки из репозитория,
            //по результатам выполнения актуализируем статус.
            _status.value                   = repositoryObject.updateFromServer()
        }
    }

    /************************************************************************************************
     * Удаление элемента из списка.                                                                 *
     ***********************************************************************************************/
    fun delete(
        item                                : CObject
    )                                       = viewModelScope.launch {
        repositoryObject.delete(item)
    }


}