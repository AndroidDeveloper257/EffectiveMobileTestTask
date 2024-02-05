package com.example.effectivemobiletesttask.repository

import com.example.effectivemobiletesttask.networking.ApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getProducts() = apiService.getProducts()

}