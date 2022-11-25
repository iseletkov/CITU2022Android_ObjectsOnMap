package ru.permkrai.it.android.objectsonmap.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.permkrai.it.android.objectsonmap.repositories.CRepositoryObjects

class CViewModelFactory (
    private val repositoryObjects           : CRepositoryObjects
    )                                       : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    )                                       : T
    {
        if (modelClass.isAssignableFrom(CViewModelActivityList::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CViewModelActivityList(repositoryObjects) as T
        }
        if (modelClass.isAssignableFrom(CViewModelObjectInfo::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CViewModelObjectInfo(repositoryObjects) as T
        }
        if (modelClass.isAssignableFrom(CViewModelActivityMap::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CViewModelActivityMap(repositoryObjects) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}