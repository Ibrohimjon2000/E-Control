package uz.devapp.e_control

import android.app.Application
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp
import uz.devapp.e_control.database.AppDatabase

@HiltAndroidApp
class MyApp : Application() {
    companion object {
        lateinit var app: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        app=this
    }
}