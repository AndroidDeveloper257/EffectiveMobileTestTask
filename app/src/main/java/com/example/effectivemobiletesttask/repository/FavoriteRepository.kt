package com.example.effectivemobiletesttask.repository

import com.example.effectivemobiletesttask.database.dao.ItemDao
import com.example.effectivemobiletesttask.database.entity.ItemEntity
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val itemDao: ItemDao
) {

    fun saveItem(itemId: String) {
        itemDao.addItem(ItemEntity(itemId))
    }

    fun unSaveItem(itemId: String) {
        itemDao.deleteItem(ItemEntity(itemId))
    }

    fun listSavedItems() = itemDao.getItems()

    fun countSavedItems() = itemDao.countItems()

    fun isItemSaved(itemId: String): Boolean {
        return itemDao.getItems().contains(ItemEntity(itemId))
    }

}