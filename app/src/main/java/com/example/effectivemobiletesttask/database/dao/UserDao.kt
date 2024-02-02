package com.example.effectivemobiletesttask.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.effectivemobiletesttask.database.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(userEntity: UserEntity)

    @Query("SELECT * FROM user_table")
    fun getUser(): UserEntity

    @Delete
    fun deleteUser(userEntity: UserEntity)

}