package estudiante.com.finalproject
import com.google.gson.annotations.SerializedName

data class AqiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: AqiData?
)

//datos recopilados
data class AqiData(
    @SerializedName("aqi") val aqi: Int,
    @SerializedName("idx") val idx: Int,
    @SerializedName("city") val city: AqiCity,
    @SerializedName("dominentpol")val dominentpol: String?,
    @SerializedName("iaqi") val iaqi: AqiIaqi?,
    @SerializedName("time") val time: AqiTime?
)

//ciudad
data class AqiCity(
    @SerializedName("name") val name: String,
    @SerializedName("geo")  val geo: List<Double>,
    @SerializedName("url")  val url: String?
)

data class AqiIaqi(
    @SerializedName("pm25") val pm25: AqiValue?,
    @SerializedName("pm10") val pm10: AqiValue?,
    @SerializedName("no2") val no2:  AqiValue?,
    @SerializedName("o3") val o3:   AqiValue?,
    @SerializedName("so2") val so2:  AqiValue?,
    @SerializedName("co") val co:   AqiValue?,
    //temp
    @SerializedName("t") val temp: AqiValue?,
    //humedad %
    @SerializedName("h") val hum:  AqiValue?,
    //viento m/s
    @SerializedName("w") val wind: AqiValue?,
    //presión hPa
    @SerializedName("p") val pres: AqiValue?
)

data class AqiValue(
    @SerializedName("v") val v: Double
)

//fecha
data class AqiTime(
// "YYYY-MM-DD HH:MM:SS"
    @SerializedName("s")   val readable: String?,
    @SerializedName("tz")  val timezone: String?,
    @SerializedName("iso") val iso: String? // ISO 8601
)