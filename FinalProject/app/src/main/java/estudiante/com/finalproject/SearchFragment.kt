package estudiante.com.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import estudiante.com.finalproject.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val adapter = FavoriteAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPollutants.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPollutants.adapter = adapter

        // OBSERVAR RESULTADOS DE BÚSQUEDA
        viewModel.searchResults.observe(viewLifecycleOwner) { stations ->
            binding.rvPollutants.adapter = FavoriteAdapter(
                stations = stations,
                onClick = { station ->
                    // Un clic: Ver detalles de la estación buscada
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
                onDoubleClick = { station ->
                    val prefs = requireActivity().getSharedPreferences("AppPrefs", android.content.Context.MODE_PRIVATE)
                    if (prefs.getBoolean("isLoggedIn", false)) {
                        // ACCIÓN 2 (Doble clic): Agregar a tus Favoritos
                        homeViewModel.addFav(station)
                        Toast.makeText(requireContext(), "${station.name} agregado a favoritos", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Inicia sesión para guardar favoritos", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        // OBSERVAR ERRORES
        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (msg != null) Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        // CONFIGURAR LA BARRA DE BÚSQUEDA
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.searchStations(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}