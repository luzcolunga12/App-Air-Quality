package estudiante.com.finalproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntitySt::class], version = 1)
abstract class AppDatabase : RoomDatabase()
{

    abstract fun stationDao(): DaoSt

    companion object
    {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase
        {
            return INSTANCE ?: synchronized(this)
            {
                Room.databaseBuilder(

                    context.applicationContext,
                    AppDatabase::class.java,
                    "aqi_database"

                ).build().also { INSTANCE = it }
            }
        }
    }
}