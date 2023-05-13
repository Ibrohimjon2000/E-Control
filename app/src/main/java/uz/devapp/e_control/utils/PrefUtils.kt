package uz.devapp.e_control.utils

import com.orhanobut.hawk.Hawk
import uz.devapp.e_control.MyApp

object PrefUtils {
    const val PREF_TOKEN = "token"
    const val PREF_ID = "id"

    fun init() {
        Hawk.init(MyApp.app).build()
    }

    fun setToken(value: String?) {
        Hawk.put(PREF_TOKEN, value)
    }

    fun getToken(): String {
        return Hawk.get(PREF_TOKEN, "")
    }

    fun setId(value: Int?) {
        Hawk.put(PREF_ID, value)
    }

    fun getId(): Int {
        return Hawk.get(PREF_ID,0)
    }

    fun clear() {
        Hawk.deleteAll()
    }
}