package estudiante.com.finalproject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test


class TestAQI {

    //1. Verifica valor de aqi sea asignado correctamente con aqiValue
    @Test
    fun aqiValorCorrecto() {
        // Arrange
        val valorEsperado= 45.5
        // Act
        val valores = AqiValue(valorEsperado)
        // Assert
        assertEquals(45.5, valores.v, 0.0)
    }

    //2. como en aqi time los campos son opcionales,  verifica que la app no se rompa cuando la API manda null
    @Test
    fun aqiTimeValoresNull()
    {
        //Arange
        val valores_readable= null
        val valores_timezone=null
        val valores_iso=null
        //Act
        val valores = AqiTime(valores_readable, valores_timezone, valores_iso)
        //Assert
        assertNull(valores.readable)
        assertNull(valores.timezone)
        assertNull(valores.iso)

    }

    //3. aqi city guarda correctamente nombres y coordenadas
    @Test
    fun aqiCityDataCorrect()
    {
        //Arrange
        val nombre = "Monterrey"
        val geos = listOf(25.6, -100.3)
        //Act
        val valores = AqiCity(nombre, geos, null)
        //Assert
        assertEquals(nombre, valores.name)
        assertEquals(geos, valores.geo)
        assertNull(valores.url)
    }

    //4.AqiSearchResult guarda correctamente el AQI
    @Test
    fun aqiResearchCorrect()
    {
        //Arrange
        val estacion = AqiSearchStation("Cumbres", listOf(25.7, -100.4))
        val aqiEsperado = "92"
        //Act
        val valores = AqiSearchResult(estacion, aqiEsperado)
        //Assert
        assertEquals(estacion, valores.station)
        assertEquals(aqiEsperado, valores.aqi)

    }

    //5. verifica que AqiTime con todos los campos llenos guarda correctamente el readable
    @Test
    fun aqiTimeCampos()
    {
        //Arrange
        val stringEsperado = "2026-05-14 08:00:00"
        //Act
        val valores = AqiTime(stringEsperado, "-06:00", "2026-05-14T08:00:00-06:00")
        //Assert
        assertEquals(stringEsperado, valores.readable)

    }

    //6. confirmar que AqiSearchStation guarda correctamente las coordenadas en el índice correcto — que geo[0] sea la latitud y geo[1] sea la longitud
    @Test
    fun aqiSearchGeosCorrect()
    {
        //Arrange
        val name = "Monterrey"
        val geo1 = 25.6
        val geo2= -100.3
        //Act
        val lista = listOf(geo1, geo2)
        val valores = AqiSearchStation(name, lista)
        //Assert
        assertEquals(geo1, valores.geo[0], 25.6)
        assertEquals(geo2, valores.geo[1], -100.3)

    }

    //7. Aqivalue guarda el valor de 0, es importante ya q es un caso límite válido que significa aire perfectamente limpio
    @Test
    fun aqiValorLimite()
    {
        // Arrange
        val valorLimite= 0.0
        // Act
        val valores = AqiValue(valorLimite)
        // Assert
        assertEquals(0.0, valores.v, 0.0)
    }

    //8.Verifica que AqiSearchResult con AQI válido se puede convertir a Int
    @Test
    fun aqiSearchValido() {
        //Arrange
        val stat = AqiSearchStation("Monterrey", listOf(25.6, -100.3))
        val stringval = "92"
        //Act
        val valores = AqiSearchResult(stat, stringval)
        //Assert
        assertNotNull(valores.aqi.toIntOrNull())
        assertEquals(92, valores.aqi.toIntOrNull())
    }

    //9. AqiCity con url no nula, es decir con datos  guarda correctamente la url
    @Test
    fun aqiCityUrl()
    {
        //Arrange
        val nombre = "Monterrey"
        val geos = listOf(25.6, -100.3)
        val url = "https://aqicn.org/city/monterrey/"
        //Act
        val valores = AqiCity(nombre, geos, url)
        //Assert
        assertEquals(nombre, valores.name)
        assertEquals(geos, valores.geo)
        assertEquals(url, valores.url)
    }

    //10.Verifica que Station con aqi inválido como "--" devuelve null al convertir
    @Test
    fun aqiStationAqiInvalido()
    {
        //Arange
        val station = Station("Centro", "Monterrey", "--", "Buena", 0)
        //Act
        val valores = station.aqi.toIntOrNull()
        //Assert
        assertNull(valores)

    }

}