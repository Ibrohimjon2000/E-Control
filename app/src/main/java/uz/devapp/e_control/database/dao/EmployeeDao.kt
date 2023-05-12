package uz.devapp.e_control.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.devapp.e_control.database.entity.EmployeeEntity

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEmployees(list: List<EmployeeEntity>)

    @Query("select * from employeeEntity")
    fun getEmployees(): List<EmployeeEntity>

}