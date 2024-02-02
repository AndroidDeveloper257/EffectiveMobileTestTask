package com.example.effectivemobiletesttask.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.effectivemobiletesttask.database.dao.UserDao
import com.example.effectivemobiletesttask.database.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}