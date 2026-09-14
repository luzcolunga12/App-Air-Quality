package estudiante.com.finalproject

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService
{

    @GET("feed/{city}/")
    suspend fun getAqiByCity(
        @Path("city") city: String,
        @Query("token") token: String
    ):  Response<AqiResponse>

    @GET("search/")

    suspend fun searchStations(
        @Query("keyword") keyword: String,
        @Query("token") token: String

    ):Response<AqiSearchResponse>
}