package estudiante.com.finalproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import estudiante.com.finalproject.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail)
{

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        // Recuperar los datos del Bundle
        val name = arguments?.getString("name") ?: "Estación"
        val aqiString = arguments?.getString("aqi") ?: "0"
        val status = arguments?.getString("status") ?: "N/A"
        val aqiInt = aqiString.toIntOrNull() ?: 0

        // Asignar textos básicos
        binding.tvDetailName.text = name
        binding.tvDetailAqi.text = aqiString
        binding.tvDetailStatus.text = status

        // Generar recomendaciones dinámicas según el AQI
        binding.tvDetailRecommendations.text = getHealthTips(aqiInt)

        // Botón Volver
        binding.btnBack.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        // Cambiar color del texto del AQI según el rango
        val colorId = when {
            aqiInt <= 50 -> R.color.aqi_good
            aqiInt <= 100 -> R.color.aqi_moderate
            aqiInt <= 150 -> R.color.aqi_unhealthy_sensitive
            aqiInt <= 200 -> R.color.aqi_unhealthy
            aqiInt <= 300 -> R.color.aqi_very_unhealthy
            else -> R.color.aqi_hazardous
        }
        binding.tvDetailAqi.setTextColor(resources.getColor(colorId, null))
    }

    private fun getHealthTips(aqi: Int): String
    {
        return when
        {
            aqi <= 50 -> "• Calidad satisfactoria.\n• Disfruta actividades al aire libre.\n• Puedes ventilar tu hogar."
            aqi <= 100 -> "•  Calidad moderada.\n• Grupos sensibles deben reducir esfuerzos prolongados.\n• Es seguro para la mayoría de las personas."
            aqi <= 150 -> "• Insalubre para grupos sensibles.\n• Niños y adultos mayores deben limitar esfuerzo físico exterior.\n• El público general no suele verse afectado."
            aqi <= 200 -> "• Calidad mala.\n• Todos deben reducir actividades físicas al aire libre.\n• Usa cubrebocas si eres sensible."
            aqi <= 300 -> "•  Muy insalubre.\n• Evita salir de casa si no es necesario.\n• Mantén ventanas cerradas y usa purificadores."
            else -> "• PELIGRO: Riesgo extremo.\n• Permanece en interiores.\n• Evita cualquier tipo de esfuerzo físico."
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}