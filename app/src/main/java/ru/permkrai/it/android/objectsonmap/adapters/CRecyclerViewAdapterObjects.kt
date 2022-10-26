package ru.permkrai.it.android.objectsonmap.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.permkrai.it.android.objectsonmap.databinding.RecyclerviewobjectsItemBinding
import ru.permkrai.it.android.objectsonmap.model.CObject

/********************************************************************************************************
 * Класс адаптер для списка элементов. Содержит логику выбора элементов, логику вывода информации       *
 * об элементе в строку списка.                                                                         *
 * @link https://developer.alexanderklimov.ru/android/views/recyclerview-kot.php                        *
 * @link https://medium.com/nuances-of-programming/kotlin-реализация-recyclerview-на-android-6c93981e9abf
 *******************************************************************************************************/
class CRecyclerViewAdapterObjects
/********************************************************************************************************
 * Конструктор.                                                                                         *
 * @param items - список элементов данных, информацию по которым нужноо выводить на экран.              *
 *******************************************************************************************************/
(
    private val items                       : MutableList<CObject>
)                                           : RecyclerView.Adapter<CRecyclerViewAdapterObjects.CViewHolderObject>()
{
    /****************************************************************************************************
     * Вспомогательный класс, оотвечающий за визуальное отображение одного элемента данных.             *
     ***************************************************************************************************/
    class CViewHolderObject
    /****************************************************************************************************
     * Конструктор.                                                                                     *
     * @param binding - объект, хранящий ссылки на элементы интерфейса, у которых указан идентификатор. *
     ***************************************************************************************************/
    (
        private val binding                 : RecyclerviewobjectsItemBinding
    )                                       : RecyclerView.ViewHolder(binding.root)
    {
        private lateinit var item           : CObject
        /************************************************************************************************
         * Метод описывает логику вывода элемента данных в строку списка.                               *
         * @param newItem - элемент данных для вывода.                                                  *
         ***********************************************************************************************/
        fun bind(
            newItem                         : CObject
        )
        {
            item                            = newItem
            binding.textViewName.text       = newItem.name
            binding.textViewDescription.text= newItem.description
        }
    }
    /****************************************************************************************************
     * Метод вызывается в момент создания новой строки в списке.                                        *
     * Указывает, какой файл с разметкой внешнего вида использовать.                                    *
     * @param parent - ссылка на родительский элемент - RecyclerView.                                   *
     ***************************************************************************************************/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CViewHolderObject {
        val binding                         = RecyclerviewobjectsItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return CViewHolderObject(binding)
    }

    /****************************************************************************************************
     * Метод вызывается в момент назначения элемента данных с порядковым номером position на вывод в    *
     * строке списка holder.                                                                            *
     * @param holder - строка списка с управляющими графическими элементами.                            *
     * @param position - порядковый номер элемента данных в списке.                                     *
     ***************************************************************************************************/
    override fun onBindViewHolder(holder: CViewHolderObject, position: Int) {
        holder.bind(items[position])
    }
    /****************************************************************************************************
     * Возвращает актуальное количество элементов в списке.                                             *
     * @return общее количество элементов данных в списке.                                              *
     ***************************************************************************************************/
    override fun getItemCount(): Int {
        return items.size
    }
}