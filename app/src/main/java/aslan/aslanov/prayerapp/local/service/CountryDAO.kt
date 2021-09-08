package aslan.aslanov.prayerapp.local.service

import androidx.lifecycle.LiveData
import androidx.room.*
import aslan.aslanov.prayerapp.model.countryModel.City
import aslan.aslanov.prayerapp.model.countryModel.Country
import aslan.aslanov.prayerapp.model.countryModel.CountryWithCities
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity

@Dao
interface CountryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(vararg country: City)

    @Query("SELECT * FROM city where countryCreatorId=:countryName")
    fun getCities(countryName: String): LiveData<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(vararg country: Country)

    @Transaction
    @Query("SELECT * FROM table_country")
    fun getCountryWithCities(): LiveData<List<CountryWithCities>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTime(vararg timings: TimingsEntity)

    @Query("SELECT * FROM timingsentity")
    fun getTimingsEntityLive(): LiveData<TimingsEntity>



}