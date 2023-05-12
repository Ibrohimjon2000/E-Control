package uz.devapp.e_control.database.dao

import androidx.room.*
import uz.devapp.e_control.database.entity.AttendsEntity
import uz.devapp.e_control.database.entity.EmployeeEntity
import uz.devapp.e_control.database.entity.PurposeEntity

@Dao
interface AttendsDao {

    @Insert
    fun addAttends(attendsEntity: AttendsEntity)

    @Query("select * from attendsEntity")
    fun getAttends(): List<AttendsEntity>

    @Delete
    fun deleteAttends(attendsEntity: AttendsEntity)
}