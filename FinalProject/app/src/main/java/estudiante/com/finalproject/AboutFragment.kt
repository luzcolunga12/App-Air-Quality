package estudiante.com.finalproject

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class AboutFragment : Fragment(R.layout.fragment_about) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnBackAbout).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}