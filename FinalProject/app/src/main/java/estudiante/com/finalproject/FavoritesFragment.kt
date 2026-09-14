package estudiante.com.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import estudiante.com.finalproject.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment()
{

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        // Configuración básica del RecyclerView
        binding.rvFavoriteStations.layoutManager = LinearLayoutManager(requireContext())

        // Observar Room (UN SOLO OBSERVER PARA TODO)
        viewModel.favorites.observe(viewLifecycleOwner) { entities ->
            val stations = entities.map {
                Station(it.name, it.city, it.aqi, it.status, it.colorResId)
            }

            // 1. LEER EL ESTADO DE LA SESIÓN (SharedPreferences)
            val prefs = requireActivity().getSharedPreferences("AppPrefs", android.content.Context.MODE_PRIVATE)
            val isLoggedIn = prefs.getBoolean("isLoggedIn", false)
            val edad = prefs.getString("userAge", "0") ?: "0"

            binding.tvPersonalizado.text = "Personalizado • $edad años"

            // 2. OCULTAR O MOSTRAR SEGÚN SESIÓN
            if (!isLoggedIn)
            {
                binding.rvFavoriteStations.visibility = View.GONE
                binding.tvHealthTips.text = "Por favor ve a perfil para hacer el login y ver tus recomendaciones."
            }
            else
            {
                // 3. CONFIGURAR EL ADAPTER
                binding.rvFavoriteStations.visibility = View.VISIBLE
                binding.rvFavoriteStations.adapter = FavoriteAdapter(
                    stations = stations,
                    onClick = { station ->
                        // Navegar al detalle
                        val detailFragment = DetailFragment().apply {
                            arguments = Bundle().apply {
                                putString("name", station.name)
                                putString("aqi", station.aqi)
                                putString("status", station.status)
                            }
                        }
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    },
                    onLongClick = { station ->
                        // Eliminar fav
                        viewModel.removeFav(station)
                        Toast.makeText(requireContext(), "${station.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()
                    }
                )

                // 4. MOSTRAR RECOMENDACIONES
                if (stations.isNotEmpty()) {
                    val aqi = stations.first().aqi.toIntOrNull() ?: 0
                    binding.tvHealthTips.text = getHealthTips(aqi)
                } else {
                    binding.tvHealthTips.text = "Agrega estaciones favoritas para ver recomendaciones personalizadas"
                }
            }
        }
    }

    // Recomendaciones salud
    private fun getHealthTips(aqi: Int): String
    {
        return when
        {

            aqi <= 50 ->
                "• Calidad del aire satisfactoria.\n" +
                        "• ¡Es un día excelente para realizar actividades al aire libre!\n" +
                        "• Puedes ventilar espacios cerrados."


            aqi <= 100 ->
                "• Calidad del aire moderada.\n" +
                        "• Alerta para personas que podrían ser excepcionalmente sensibles a la contaminación por partículas.\n" +
                        "• Personas excepcionalmente sensibles: Contemplar reducir las actividades que requieran esfuerzo prolongado o intenso al aire libre.\n" +
                        "• Para el resto de las personas: ¡Es un buen día para realizar actividades al aire libre!"


            aqi <= 150 ->
                "• Calidad de aire insalubre para grupos sensibles.\n" +
                        "• Los grupos sensibles comprenden a personas con cardiopatías o enfermedades pulmonares, adultos mayores, niños y adolescentes.\n" +
                        "• El público en general no suele verse afectado."


            aqi <= 200 ->
                "• Calidad de aire mala. Efectos en la salud para todos.\n" +
                        "• Todos deben reducir el esfuerzo físico intenso al aire libre.\n" +
                        "• Grupos sensibles deben evitar toda actividad física exterior."


            aqi <= 300 ->
                "• Calidad de aire muy mala. Efectos en la salud para todos.\n" +
                        "• Todos deben evitar el esfuerzo físico al aire libre.\n" +
                        "• Mantén ventanas cerradas y usa purificadores de aire (HEPA)."

            else ->
                "• Advertencia de salud: condiciones de riesgo extremo para todos.\n" +
                        "• Permanece en interiores con el menor movimiento posible.\n" +
                        "• Sella filtraciones de aire en puertas y ventanas."

        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}