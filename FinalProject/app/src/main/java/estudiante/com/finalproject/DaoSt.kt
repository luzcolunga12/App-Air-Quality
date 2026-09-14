package estudiante.com.finalproject

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoSt {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: EntitySt)

    @Delete
    suspend fun delete(station: EntitySt)

    @Query("SELECT * FROM stations")
    fun getAllFavorites(): LiveData<List<EntitySt>>

    @Query("SELECT COUNT(*) FROM stations WHERE name = :name")
    suspend fun isFavorite(name: String): Int
}
