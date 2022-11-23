package ru.permkrai.it.android.objectsonmap.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.repositories.CRepositoryObjects
import java.util.*

/********************************************************************************************************
 * Модель представления (отображаемых данных и состояния) для активности редактирования объекта.        *
 *******************************************************************************************************/
class CViewModelObjectInfo(
    private val repositoryObject            : CRepositoryObjects
)                                           : ViewModel()
{
    //Переменная хранит идентификатор, который даёт активность из своих параметров.
    //Обновляется через setId.
    //Оформлена в виде потока, чтобы на изменение id можно было реагировать другим потоком.
    private val id                          = MutableStateFlow<UUID?>(null)
    //Переменная зранит поток, в котором будут передаваться значения элемента.
    val item                                : StateFlow<CObject>

                                            = id //Каждый раз при изменении id
        .flatMapLatest { value ->
            //Если id null - создаём поток из одного нового объекта
            if (value == null) {
                flow{ CObject()}
            } else {
                //Если идентификатор нормальный, берём поток из БД
                repositoryObject.getById(value)
            }
        }
        //Обычный поток переводим в поток с сохранением последнего состояния.
            .stateIn(
                scope                       = viewModelScope,
                started                     = WhileSubscribed(5000),
                initialValue                = CObject()
            )


    /****************************************************************************************************
     * Установка идентификатора объекта.                                                                *
     ***************************************************************************************************/
    fun setId(
        id                                  : UUID?
    ) {
        this.id.value                       = id
    }
    /****************************************************************************************************
     * Запись полей из формы в объект, сохранение объекта в БД.                                         *
     ***************************************************************************************************/
    fun save(
        name                                : String
    )
    {
        //Запись осуществляем в потоке ввода-вывода, чтобы не зависало окно приложения.
        viewModelScope.launch(Dispatchers.IO)
        {
            //Обновляем свойства объекта по данным из формы (Надо DataBinding)
            item.value.name                 = name
            //Если идентификатор есть - вызываем обновление в БД.
            id.value?.let {
                repositoryObject.update(item.value)
            } ?:
            //Если идентификатора нет, вызываем сохранение в БД.
            run{
                repositoryObject.insert(item.value)
            }
        }
    }
}