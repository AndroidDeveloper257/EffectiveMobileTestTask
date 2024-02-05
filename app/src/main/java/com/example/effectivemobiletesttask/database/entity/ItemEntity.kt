package com.example.effectivemobiletesttask.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class ItemEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "item_id")
    val id: String,
)