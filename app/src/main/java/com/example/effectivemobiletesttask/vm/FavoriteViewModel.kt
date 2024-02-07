package com.example.effectivemobiletesttask.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectivemobiletesttask.database.entity.ItemEntity
import com.example.effectivemobiletesttask.models.ResponseData
import com.example.effectivemobiletesttask.repository.FavoriteRepository
import com.example.effectivemobiletesttask.repository.ProductRepository
import com.example.effectivemobiletesttask.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val productRepository: ProductRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _savedProductsLiveData = MutableLiveData<List<ItemEntity>>()
    val savedProductLiveData = _savedProductsLiveData

    private var responseData: ResponseData? = null

    init {
        listSavedProducts()
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                val response = productRepository.getProducts()
                if (response.isSuccessful) {
                    responseData = response.body()
                }
            }
        }
    }

    private fun listSavedProducts() =
        _savedProductsLiveData.postValue(favoriteRepository.listSavedItems())

    fun saveItem(itemId: String) = favoriteRepository.saveItem(itemId)

    fun unSaveItem(itemId: String) = favoriteRepository.unSaveItem(itemId)

    fun countSavedItems() = favoriteRepository.countSavedItems()

    fun isItemSaved(itemId: String) = favoriteRepository.isItemSaved(itemId)

    fun clearFavorite() = favoriteRepository.clearTable()

}