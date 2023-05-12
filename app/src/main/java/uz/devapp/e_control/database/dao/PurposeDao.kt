package uz.devapp.e_control.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.devapp.e_control.database.entity.EmployeeEntity
import uz.devapp.e_control.database.entity.PurposeEntity

@Dao
interface PurposeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPurpose(list: List<PurposeEntity>)

    @Query("select * from purposeEntity")
    fun getPurpose(): List<PurposeEntity>

}