package uz.devapp.e_control.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.HiltAndroidApp
import uz.devapp.e_control.database.dao.AttendsDao
import uz.devapp.e_control.database.dao.EmployeeDao
import uz.devapp.e_control.database.dao.PurposeDao
import uz.devapp.e_control.database.entity.AttendsEntity
import uz.devapp.e_control.database.entity.EmployeeEntity
import uz.devapp.e_control.database.entity.PurposeEntity

@Database(entities = [EmployeeEntity::class,PurposeEntity::class,AttendsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun purposeDao(): PurposeDao
    abstract fun attendsDao(): AttendsDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}