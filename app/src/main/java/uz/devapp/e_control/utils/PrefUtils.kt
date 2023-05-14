package uz.devapp.e_control.utils

import com.orhanobut.hawk.Hawk
import uz.devapp.e_control.MyApp
import uz.devapp.e_control.data.model.DeviceModel

object PrefUtils {
    const val PREF_TOKEN = "token"
    const val PREF_DEVICE = "device"
    const val PREF_SWITCH = "switch"

    fun init() {
        Hawk.init(MyApp.app).build()
    }

    fun setToken(value: String?) {
        Hawk.put(PREF_TOKEN, value)
    }

    fun getToken(): String {
        return Hawk.get(PREF_TOKEN, "")
    }

    fun setDevice(value: DeviceModel?) {
        Hawk.put(PREF_DEVICE, value)
    }

    fun getDevice(): DeviceModel {
        return Hawk.get(PREF_DEVICE)
    }

    fun setSwitch(value: Boolean?) {
        Hawk.put(PREF_SWITCH, value)
    }

    fun getSwitch(): Boolean {
        return Hawk.get(PREF_SWITCH,false)
    }

    fun clear() {
        Hawk.deleteAll()
    }
}