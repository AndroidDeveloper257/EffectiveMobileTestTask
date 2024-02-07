package com.example.effectivemobiletesttask.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectivemobiletesttask.database.entity.ItemEntity
import com.example.effectivemobiletesttask.models.Item
import com.example.effectivemobiletesttask.models.ResponseData
import com.example.effectivemobiletesttask.repository.FavoriteRepository
import com.example.effectivemobiletesttask.repository.ProductRepository
import com.example.effectivemobiletesttask.utils.ConsValues
import com.example.effectivemobiletesttask.utils.ConsValues.NO_INTERNET
import com.example.effectivemobiletesttask.utils.ConsValues.TAG
import com.example.effectivemobiletesttask.utils.NetworkHelper
import com.example.effectivemobiletesttask.utils.SortEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _productLiveData =
        MutableLiveData<ApiStatus<ResponseData>>(ApiStatus.Loading())
    val productLiveData get() = _productLiveData

    private var responseData: ResponseData? = null

    init {
        fetchProducts()
        Log.d(TAG, "fetching products: ")
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                val response = productRepository.getProducts()
                if (response.isSuccessful) {
                    responseData = response.body()
                    _productLiveData.postValue(ApiStatus.Success(response.body()))
                    Log.d(TAG, "fetchProducts: succeeded $responseData")
                } else {
                    _productLiveData.postValue(ApiStatus.Error(Throwable(response.message())))
                }
            } else {
                _productLiveData.postValue(ApiStatus.Error(Throwable(NO_INTERNET)))
            }
        }
    }

    fun saveItem(itemId: String) = favoriteRepository.saveItem(itemId)

    fun unSaveItem(itemId: String) = favoriteRepository.unSaveItem(itemId)

    fun listSavedItems() = favoriteRepository.listSavedItems()

    fun countSavedItems() = favoriteRepository.countSavedItems()

    fun isItemSaved(itemId: String) = favoriteRepository.isItemSaved(itemId)

    fun productById(itemId: String): Item? {
        Log.d(TAG, "productById: $responseData")
        return responseData?.items?.find { it.id == itemId }
    }

    fun getProductsByTag(tag: String, sortType: String): List<Item> {
        Log.d(TAG, "getProductsByTag: filtering tag $tag products for sortType $sortType")
        val filteredList = responseData?.items?.filter { item ->
            item.tags.contains(tag)
        } ?: emptyList()
        return sortProducts(filteredList, sortType)
    }

    fun getAllProducts(sortType: String): List<Item> {
        Log.d(TAG, "getProductsByTag: filtering all products for sortType $sortType")
        return sortProducts(responseData?.items ?: emptyList(), sortType)
    }

    private fun sortProducts(products: List<Item>, sortType: String): List<Item> {
        return when (sortType) {
            SortEnum.POPULARITY.sortType -> products.sortedByDescending { it.feedback.rating }
            SortEnum.DECREASING_PRICE.sortType -> products.sortedByDescending { it.price.priceWithDiscount.toDouble() }
            SortEnum.ASCENDING_PRICE.sortType -> products.sortedBy { it.price.priceWithDiscount.toDouble() }
            else -> products
        }
    }

    fun getSavedItems(savedItems: List<ItemEntity>): List<Item> {
        val filteredItems = mutableListOf<Item>()
        responseData?.items?.forEach { responseItem ->
            if (savedItems.any { savedItem ->
                    savedItem.id == responseItem.id
                }) {
                filteredItems.add(responseItem)
            }
        }
        Log.d(TAG, "getSavedItems: $filteredItems")
        Log.d(TAG, "getSavedItems: $responseData")
        return filteredItems
    }
}