package aslan.aslanov.prayerapp.model.hadeeths


import com.squareup.moshi.Json

data class Meta(
    @Json(name = "current_page")
    val currentPage: String?,
    @Json(name = "last_page")
    val lastPage: Int?,
    @Json(name = "per_page")
    val perPage: String?,
    @Json(name = "total_items")
    val totalItems: Int?
)