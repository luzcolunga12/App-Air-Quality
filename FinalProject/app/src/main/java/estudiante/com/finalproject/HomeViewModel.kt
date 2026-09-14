package estudiante.com.finalproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel(application: Application) : AndroidViewModel(application)
{

    //Room
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.stationDao()

    val favorites: LiveData<List<EntitySt>> = dao.getAllFavorites()

    //Rettrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.waqi.info/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    //LiveData
    private val _featuredStation = MutableLiveData<Station?>()
    val featuredStation: LiveData<Station?> = _featuredStation

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Estacio de la API
    fun loadStation(city: String)
    {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getAqiByCity(city, "d3550024bbb06195a8dec53cc9b6a01d32ace9bc")

                if (response.isSuccessful && response.body()?.status == "ok")
                {
                    val data = response.body()!!.data!!
                    val aqi = data.aqi
                    _featuredStation.postValue(
                        Station(
                            name = data.city.name,
                            city = city,
                            aqi = aqi.toString(),
                            status = aqiToStatus(aqi),
                            colorResId = aqiToColor(aqi)
                        )
                    )
                }
                else
                {
                    _errorMessage.postValue("No se encontró la ciudad")
                }
            }
            catch (e: Exception)
            {
                _errorMessage.postValue("Error de conexión: ${e.message}")
            }
        }
    }

    //AgregaFav
    fun addFav(station: Station)
    {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(EntitySt(station.name, station.city, station.aqi, station.status, station.colorResId))
        }
    }

    //QuitarFav
    fun removeFav(station: Station)
    {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(EntitySt(station.name, station.city, station.aqi, station.status, station.colorResId))
        }
    }

    //Helpers
    private fun aqiToStatus(aqi: Int): String = when
    {
        aqi <= 50  -> "Buena"
        aqi <= 100 -> "Moderada"
        aqi <= 150 -> "Insalubre (g.s.)"
        aqi <= 200 -> "Insalubre"
        aqi <= 300 -> "Muy insalubre"
        else       -> "Peligrosa"
    }

    private fun aqiToColor(aqi: Int): Int = when
    {
        aqi <= 50  -> R.color.aqi_good
        aqi <= 100 -> R.color.aqi_moderate
        aqi <= 150 -> R.color.aqi_unhealthy_sensitive
        aqi <= 200 -> R.color.aqi_unhealthy
        aqi <= 300 -> R.color.aqi_very_unhealthy
        else       -> R.color.aqi_hazardous
    }
}