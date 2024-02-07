package com.example.effectivemobiletesttask.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.effectivemobiletesttask.database.dao.ItemDao
import com.example.effectivemobiletesttask.database.dao.UserDao
import com.example.effectivemobiletesttask.database.entity.ItemEntity
import com.example.effectivemobiletesttask.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, ItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun itemDao(): ItemDao

}