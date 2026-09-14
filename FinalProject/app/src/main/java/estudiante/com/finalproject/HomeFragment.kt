package estudiante.com.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import estudiante.com.finalproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LEER EL ESTADO DE LA SESIÓN Y SALUDAR
        val prefs = requireActivity().getSharedPreferences("AppPrefs", android.content.Context.MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val nombre = prefs.getString("userName", "stranger")  ?: "stranger"
            binding.tvGreeting.text = "Buenos días, $nombre"
        }

        // CONFIGURACIÓN BÁSICA DEL RECYCLERVIEW
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())

        // OBSERVAR FAVORITOS (Y MANEJAR ESTADO VACÍO)
        viewModel.favorites.observe(viewLifecycleOwner) { entities ->
            val stations = entities.map {
                Station(it.name, it.city, it.aqi, it.status, it.colorResId)
            }

            if (stations.isEmpty()) {
                // No hay favoritos: Mostramos el mensaje lindo y ocultamos la lista
                binding.rvFavorites.visibility = View.GONE
                binding.layoutEmptyFavorites.visibility = View.VISIBLE
            } else {
                // Sí hay favoritos: Mostramos la lista y ocultamos el mensaje
                binding.layoutEmptyFavorites.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE

                // Configuramos el adaptador con ambas acciones
                binding.rvFavorites.adapter = FavoriteAdapter(
                    stations = stations,
                    onClick = { station ->
                        // Navegar al detalle (clic normal)
                        val detailFragment = DetailFragment().apply {
                            arguments = Bundle().apply {
                                putString("name", station.name)
                                putString("aqi", station.aqi)
                                putString("status", station.status)
                            }
                        }
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, detailFragment) // Asegura que este ID sea el correcto en tu MainActivity
                            .addToBackStack(null)
                            .commit()
                    },
                    onLongClick = { station ->
                        // Borrar de favoritos (dejar presionado)
                        viewModel.removeFav(station)
                        Toast.makeText(requireContext(), "${station.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // OBSERVAR ESTACIÓN DESTACADA DE LA API
        viewModel.featuredStation.observe(viewLifecycleOwner) { station ->
            if (station != null) {
                binding.tvAQIValue.text = station.aqi
                binding.tvLocation.text = station.name
            }
        }

        // OBSERVAR ERRORES DE LA API
        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (msg != null) Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        // CARGAR DATOS DE LA API AL ENTRAR
        viewModel.loadStation("monterrey")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}