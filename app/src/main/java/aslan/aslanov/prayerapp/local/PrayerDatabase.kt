package aslan.aslanov.prayerapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import aslan.aslanov.prayerapp.local.service.CountryDAO
import aslan.aslanov.prayerapp.model.countryModel.City
import aslan.aslanov.prayerapp.model.countryModel.Country
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity

@Database(entities = [Country::class, City::class, TimingsEntity::class],version = 1,exportSchema = false)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun getDao(): CountryDAO

    companion object {
        @Volatile
        private var INSTANCE: PrayerDatabase? = null

        fun getInstance(context: Context): PrayerDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PrayerDatabase::class.java,
                        "prayerDatabase"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}