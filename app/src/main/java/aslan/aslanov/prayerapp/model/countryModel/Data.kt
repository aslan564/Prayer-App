package aslan.aslanov.prayerapp.model.countryModel


import androidx.room.*
import com.squareup.moshi.Json
import java.util.*

data class CountryData(
    @Json(name = "cities")
    val cities: List<String>?,
    @Json(name = "country")
    val country: String?
) {
}

@Entity(tableName = "table_country")
data class Country(
    @PrimaryKey(autoGenerate = false)
    val country: String
)

@Entity
data class City(
    val countryCreatorId: String,
    @PrimaryKey
    val city: String
)
data class CountryWithCities(
    @Embedded var country: Country,
    @Relation(
        parentColumn = "country",
        entityColumn = "countryCreatorId"
    )
    var city: List<City>
)
