package com.example.effectivemobiletesttask.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.effectivemobiletesttask.database.entity.ItemEntity

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(itemEntity: ItemEntity)

    @Query("SELECT * FROM item_table")
    fun getItems(): List<ItemEntity>

    @Query("SELECT COUNT(*) FROM item_table")
    fun countItems(): Int

    @Delete
    fun deleteItem(itemEntity: ItemEntity)

    @Query("DELETE FROM item_table")
    fun clearTable()
}