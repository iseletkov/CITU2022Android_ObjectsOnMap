package ru.permkrai.it.android.objectsonmap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.repositories.CRepositoryObjects

class CViewModelActivityList(
    private val repositoryObject            : CRepositoryObjects
)                                           : ViewModel()
{

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allObjects                          : Flow<List<CObject>>
                                            = repositoryObject.allObjects

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(
        `object`                            : CObject
    )                                       = viewModelScope.launch {
        repositoryObject.insert(`object`)
    }


}