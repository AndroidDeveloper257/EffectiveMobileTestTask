package com.example.effectivemobiletesttask.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.effectivemobiletesttask.database.entity.UserEntity
import com.example.effectivemobiletesttask.repository.ProfileRepository
import com.example.effectivemobiletesttask.utils.ConsValues.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _userLiveData =
        MutableLiveData<UserEntity>()
    val userLiveData get() = _userLiveData

    init {
        getUser()
    }

    private fun getUser() {
        try {
            _userLiveData.postValue(profileRepository.getUser())
        } catch (e: Exception) {
            Log.e(TAG, "getUser: there's no any user yet")
        }
    }

    fun addUser(userEntity: UserEntity) = profileRepository.addUser(userEntity)

    fun deleteUser() = profileRepository.deleteUser()

}