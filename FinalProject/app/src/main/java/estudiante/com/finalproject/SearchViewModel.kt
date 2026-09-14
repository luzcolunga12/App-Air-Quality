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

class SearchViewModel(application: Application) : AndroidViewModel(application)
{

    private val api = Retrofit.Builder()
        .baseUrl("https://api.waqi.info/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    // LiveData con el resultado que arroja la busqueda
    private val _searchResults = MutableLiveData<List<Station>>()
    val searchResults: LiveData<List<Station>> = _searchResults


    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun searchStations(keyword: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.searchStations(keyword, "d3550024bbb06195a8dec53cc9b6a01d32ace9bc")
                val body = response.body()

                if (response.isSuccessful && body != null && body.status == "ok") {

                    val results = body.data.map { result ->
                        val aqiInt = result.aqi.toIntOrNull() ?: 0

                        Station(
                            name = result.station.name,
                            city = result.station.name,
                            aqi = result.aqi,
                            status = aqiToStatus(aqiInt),
                            colorResId = aqiToColor(aqiInt)
                        )
                    }

                    _searchResults.postValue(results)

                }
                else
                {
                    _errorMessage.postValue("No hay resultados para tu búsqueda")
                }

            }
            catch (e: Exception)
            {
                _errorMessage.postValue("Error en la conexión: ${e.message}")
            }
        }
    }

    private fun aqiToStatus(aqi: Int): String
    {
        if (aqi <= 50) return "Buena"
        if (aqi <= 100) return "Moderada"
        if (aqi <= 150) return "Insalubre (g.s.)"
        if (aqi <= 200) return "Insalubre"
        if (aqi <= 300) return "Muy insalubre"
        return "Peligrosa"
    }

    //color asignado debendiendo del valor aqi
    private fun aqiToColor(aqi: Int): Int =
        if (aqi <= 50) R.color.aqi_good
        else if (aqi <= 100) R.color.aqi_moderate
        else if (aqi <= 150) R.color.aqi_unhealthy_sensitive
        else if (aqi <= 200) R.color.aqi_unhealthy
        else if (aqi <= 300) R.color.aqi_very_unhealthy
        else R.color.aqi_hazardous


}