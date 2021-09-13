package aslan.aslanov.prayerapp.model.hadeeths


import com.squareup.moshi.Json

data class HadeethsResponse(
    @Json(name = "data")
    val `data`: List<Data>?,
    @Json(name = "meta")
    val meta: Meta?
)