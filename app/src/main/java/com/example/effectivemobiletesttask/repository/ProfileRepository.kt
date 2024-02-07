package com.example.effectivemobiletesttask.repository

import com.example.effectivemobiletesttask.database.dao.UserDao
import com.example.effectivemobiletesttask.database.entity.UserEntity
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun addUser(userEntity: UserEntity) = userDao.addUser(userEntity)

    fun getUser() = userDao.getUser()

    fun deleteUser() = userDao.deleteUser(getUser())

}