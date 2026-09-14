package estudiante.com.finalproject
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class EntitySt(
    @PrimaryKey val name: String,
    val city: String,
    val aqi: String,

    val status: String,

    val colorResId: Int
)