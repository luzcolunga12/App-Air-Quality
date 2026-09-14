package estudiante.com.finalproject

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import estudiante.com.finalproject.databinding.ItemFavoriteListBinding

class FavoriteAdapter(
    private val stations: List<Station>,
    private val onClick: (Station) -> Unit = {},         // 1 Clic (Detalles)
    private val onLongClick: (Station) -> Unit = {},     // Dejar presionado (Borrar)
    private val onDoubleClick: (Station) -> Unit = {}    // Doble clic (Agregar Favorito)
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(val binding: ItemFavoriteListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder
    {
        val binding = ItemFavoriteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int)
    {
        val station = stations[position]

        with(holder.binding){

            tvStationName.text = station.name
            tvStationCity.text = station.city
            tvAqiScore.text = station.aqi
            tvAqiStatus.text = station.status
            colorIndicator.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, station.colorResId))
        }

        var clickCount = 0

        val handler = Handler(Looper.getMainLooper())

        val runnable = Runnable{

            if (clickCount == 1) {
                // Si después de 300ms solo hubo 1 clic, navegamos al detalle
                onClick(station)
            }
            clickCount = 0 // Reiniciamos el contador
        }

        holder.itemView.setOnClickListener {
            clickCount++

            if (clickCount == 1)
            {
                // Es el primer clic. Esperamos 300 milisegundos por el segundo.
                handler.postDelayed(runnable, 300)
            }
            else if (clickCount == 2)
            {
                // ¡Doble clic detectado!
                onDoubleClick(station)
                clickCount = 0
                handler.removeCallbacks(runnable) // Cancelamos la navegacion
            }
        }

        // Long Click
        holder.itemView.setOnLongClickListener{
            onLongClick(station)
            true
        }
    }

    override fun getItemCount(): Int = stations.size
}