package uz.devapp.e_control.data.repository.sealed

sealed class DataResult<T> {
    class LoadingShow<T> : DataResult<T>()
    class LoadingHide<T> : DataResult<T>()
    data class Success<T>(val result: T) : DataResult<T>()
    data class Error<T>(val message: String) : DataResult<T>()
}