package aslan.aslanov.prayerapp.network

data class NetworkResult<T>(
    val data: T?,
    val msg: String?,
    val status: Status,
) {
    companion object {
        fun <T> success(data: T?): NetworkResult<T> {
            return NetworkResult(data, null, Status.SUCCESS)
        }

        fun <T> loading(): NetworkResult<T> {
            return NetworkResult(null, null, Status.LOADING)
        }

        fun <T> error(errorMsg: String?): NetworkResult<T> {
            return NetworkResult(null, errorMsg, Status.ERROR)
        }
    }
}

enum class Status {
    SUCCESS, ERROR, LOADING
}