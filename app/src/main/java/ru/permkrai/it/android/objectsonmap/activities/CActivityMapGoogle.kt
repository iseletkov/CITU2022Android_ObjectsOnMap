package ru.permkrai.it.android.objectsonmap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import ru.permkrai.it.android.objectsonmap.CApplication
import ru.permkrai.it.android.objectsonmap.R
import ru.permkrai.it.android.objectsonmap.databinding.ActivityMapBinding
import ru.permkrai.it.android.objectsonmap.model.CObject
import ru.permkrai.it.android.objectsonmap.viewmodels.CViewModelActivityMap
import ru.permkrai.it.android.objectsonmap.viewmodels.CViewModelFactory
/********************************************************************************************************
 * Активность с отображением списка объектов на карте Google.                                           *
 *******************************************************************************************************/
class CActivityMapGoogle : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    //Получение ссылки на экземляр класса CViewModelActivityList
    private val viewModel                   : CViewModelActivityMap by viewModels {
        CViewModelFactory((application as CApplication).repositoryObjects)
    }
    //Список ссылок на маркеры, чтобы их можно было удалить с карты.
    val markers                             : MutableList<Marker>
                                            = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    }
    /****************************************************************************************************
     * Обновление маркеров на карте.                                                                    *
     * @param newItems - новый список объектов, для которых нужно вывести маркеры.                      *
     ***************************************************************************************************/
    private fun refreshMarkers(
        newItems                            : List<CObject>
    )
    {
        //Если есть хотя бы один маркер - перемещаем камеру к первому из списка.
        if (newItems.isNotEmpty()) {
            val position = LatLng(newItems[0].latitude, newItems[0].longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10F))
        }
        //Удаляем маркеры, для которых объекты не встречаются в списке.
        var flag = false;
        for (i in markers.size-1 downTo 0) {
            flag = true
            for (item in newItems)
            {
                if (item.id.toString()==markers[i].tag)
                    flag = false
            }
            if (flag) {
                //Удаление маркера с карты
                markers[i].remove()
                //Удаление маркера из списка.
                markers.removeAt(i)
            }
        }
        //Создаём маркеры для объектов, для которых ещё не было маркеров.
        newItems
            //Здесь оставляем только те объекты, для которых нет маркеров.
            .filter{item->
                for (marker in markers)
                {
                    if (item.id.toString()==marker.tag)
                        return@filter false
                }
                true
            }
            //Перебираем все оставшиеся объекты и создаё м маркеры.
            .forEach {item->
                //Координаты маркера - из объекта.
                val position = LatLng(item.latitude, item.longitude)
                //Собственно создание маркера.
                val marker = mMap
                    .addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(item.name)
                    )
                //Если маркер создан нормально, не равен null
                marker?.let{
                    //Добавляем маркер в список маркеров
                    markers.add(it)
                    //Запоминаем идентификатор соответствующего объекта.
                    it.tag = item.id
                }


            }

    }
}