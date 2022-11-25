package ru.permkrai.it.android.objectsonmap.activities


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.launch
import ru.permkrai.it.android.objectsonmap.CApplication
import ru.permkrai.it.android.objectsonmap.databinding.ActivityMapYandexBinding
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.viewmodels.CViewModelActivityMap
import ru.permkrai.it.android.objectsonmap.viewmodels.CViewModelFactory

/********************************************************************************************************
 * Активность с отображением списка объектов на карте Yandex.                                           *
 *******************************************************************************************************/
class CActivityMapYandex                    : AppCompatActivity() {
    //Объект класса, содержащий сылки на управляющие графические элементы интерфейса пользователя.
    private lateinit var binding            : ActivityMapYandexBinding

    //Получение ссылки на экземляр класса CViewModelActivityMap
    //По-хорошему, на каждую активность долна быть своя отдельная модель представления.
    private val viewModel                   : CViewModelActivityMap by viewModels {
        CViewModelFactory((application as CApplication).repositoryObjects)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Здесь надо указать свой ключ API Yandex
        MapKitFactory.setApiKey("d890efcf-XXXX-4XXX-XXXX-XXXXXXXXXXXX")
        MapKitFactory.initialize(this);

        binding                             = ActivityMapYandexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Выставление камеры в позицию по-умолчанию.
        binding.map.map.move(
            CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
        /************************************************************************************************
         * Обработка изменений списка объектов в базе данных.                                           *
         ***********************************************************************************************/
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allObjects.collect { newItems ->
                    refreshMarkers(newItems)
                }
            }
        }

    }
    override fun onStop() {
        binding.map.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.map.onStart()
    }
    /****************************************************************************************************
     * Обновление маркеров на карте.                                                                    *
     * @param newItems - новый список объектов, для которых нужно вывести маркеры.                      *
     ***************************************************************************************************/
    private fun refreshMarkers(
        newItems                            : List<CObject>
    )
    {
        if (newItems.isNotEmpty())
        {
            //Выставление камеры в позицию, соответствующую первому маркеру.
            binding.map.map.move(
                CameraPosition(Point(newItems[0].latitude, newItems[0].longitude), 11.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0F),
                null
            )
        }
        //Удаляем все старые объекты с карты
        binding.map.map.mapObjects.clear()
        //Добавляем все маркеры заново.
        newItems
            .forEach {item ->
                val point = Point(item.latitude, item.longitude)
                binding.map.map.mapObjects.addPlacemark(point)
            }

    }
}