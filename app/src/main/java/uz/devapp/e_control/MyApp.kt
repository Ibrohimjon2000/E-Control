package uz.devapp.e_control

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import uz.devapp.e_control.database.AppDatabase

@HiltAndroidApp
class MyApp : Application() {
    companion object {
        lateinit var app: MyApp
    }
}