package ru.permkrai.it.android.objectsonmap.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.repositories.CRepositoryObjects
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


    /************************************************************************************************
     * Удаление элемента из списка.                                                                 *
     ***********************************************************************************************/
    fun delete(
        item                                : CObject
    )                                       = viewModelScope.launch {
        repositoryObject.delete(item)
    }


}