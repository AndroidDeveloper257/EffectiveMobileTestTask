package com.example.effectivemobiletesttask.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectivemobiletesttask.models.ResponseData
import com.example.effectivemobiletesttask.repository.ProductRepository
import com.example.effectivemobiletesttask.utils.ConsValues.NO_INTERNET
import com.example.effectivemobiletesttask.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _productLiveData =
        MutableLiveData<ApiStatus<ResponseData>>(ApiStatus.Loading())
    val productLiveData get() = _productLiveData

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                val response = productRepository.getProducts()
                if (response.isSuccessful) {
                    _productLiveData.postValue(ApiStatus.Success(response.body()))
                } else {
                    _productLiveData.postValue(ApiStatus.Error(Throwable(response.message())))
                }
            } else {
                _productLiveData.postValue(ApiStatus.Error(Throwable(NO_INTERNET)))
            }
        }
    }

}