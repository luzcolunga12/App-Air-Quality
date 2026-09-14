package estudiante.com.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import estudiante.com.finalproject.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        // Verificamos el estado al entrar al Fragment (mostrará Perfil o Login)
        updateUI()

        // LÓGICA DE LOGIN
        binding.btnLogin.setOnClickListener{
            setLoggedIn(true)
            Toast.makeText(requireContext(), "¡Sesión iniciada con éxito!", Toast.LENGTH_SHORT).show()
            updateUI()
        }

        binding.tvGoToRegister.setOnClickListener{
            // Ocultamos Login, mostramos Registro
            binding.layoutLogin.visibility = View.GONE
            binding.layoutRegister.visibility = View.VISIBLE
        }


        // LÓGICA DE REGISTRO
        binding.btnRegister.setOnClickListener{
            val nombre = binding.etRegisterName.text.toString()
            val email = binding.etRegisterEmail.text.toString()

            val edad = binding.etRegisterAge.text.toString()

            android.util.Log.d("ProfileFragment", "Guardando email: $email")


            val prefs = requireActivity().getSharedPreferences("AppPrefs", android.content.Context.MODE_PRIVATE)
            prefs.edit()
                .putString("userName", nombre)
                .putString("userEmail", email)
                .putString("userAge", edad)
                .apply()

            prefs.edit().putString("userAge", edad).apply()

            setLoggedIn(true)
            Toast.makeText(requireContext(), "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show()
            updateUI()
        }


        binding.tvGoToLogin.setOnClickListener {
            // Ocultamos Registro, mostramos Login
            binding.layoutRegister.visibility = View.GONE
            binding.layoutLogin.visibility = View.VISIBLE
        }


        // LÓGICA DEL PERFIL
        binding.btnLogout.setOnClickListener{
            setLoggedIn(false)
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
            updateUI()
        }


        // KOGIC ABOUT
        binding.btnOptionAbout?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AboutFragment())
                .addToBackStack(null)
                .commit()
        }
    }


    // Esta función decide qué pantalla principal mostrar (Perfil vs Auth)
    private fun updateUI()
    {
        if (isUserLoggedIn()) {
            binding.layoutLogin.visibility = View.GONE
            binding.layoutRegister.visibility = View.GONE
            binding.layoutProfile.visibility = View.VISIBLE

            // Leer nombre guardado y mostrarlo dinámicamente
            val prefs = requireActivity().getSharedPreferences("AppPrefs", android.content.Context.MODE_PRIVATE)
            val nombre = prefs.getString("userName", "Usuario") ?: "Usuario"
            binding.tvUserName.text = nombre
            binding.tvInitials.text = nombre.take(2).uppercase()


            // Email
            val email = prefs.getString("userEmail", "sin correo") ?: "sin correo"
            binding.tvUserEmail.text = email

            //edad
            val edad = prefs.getString("userAge", "") ?: ""
            binding.tvUserEmail.text = "$email • $edad años"



        }
        else {
            binding.layoutProfile.visibility = View.GONE
            binding.layoutRegister.visibility = View.GONE
            binding.layoutLogin.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "Por favor, inicia sesión para ver tu perfil", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

    // HELPERS PARA LEER Y GUARDAR SESIÓN GLOBAL
    private fun isUserLoggedIn(): Boolean
    {
        val prefs = requireActivity().getSharedPreferences("AppPrefs", android.content.Context.MODE_PRIVATE)
        return prefs.getBoolean("isLoggedIn", false)

    }


    private fun setLoggedIn(state: Boolean)
    {
        val prefs = requireActivity().getSharedPreferences("AppPrefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putBoolean("isLoggedIn", state).apply()
    }
}