package ru.permkrai.it.android.objectsonmap.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.repositories.CRepositoryObjects

class CViewModelActivityMap(
    private val repositoryObject            : CRepositoryObjects
)                                           : ViewModel() {
    //Поток со списками всех объектов.
    val allObjects                          : Flow<List<CObject>>
                                            = repositoryObject.getAll()

}