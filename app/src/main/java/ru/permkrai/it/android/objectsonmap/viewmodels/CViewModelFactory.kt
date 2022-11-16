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
//        if (modelClass.isAssignableFrom(CViewModelActivityList::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return CViewModelActivityList(repositoryObjects) as T
//        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}