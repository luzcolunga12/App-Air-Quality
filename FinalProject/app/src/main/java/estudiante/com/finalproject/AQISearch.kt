package estudiante.com.finalproject

import com.google.gson.annotations.SerializedName

data class AqiSearchResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<AqiSearchResult>
)


//datos recopikados de la busqueda
data class AqiSearchResult(
    @SerializedName("station") val station: AqiSearchStation,
    @SerializedName("aqi") val aqi: String
)


//station busqueda
data class AqiSearchStation(
    @SerializedName("name") val name:  String,
    @SerializedName("geo") val geo:  List<Double>
)