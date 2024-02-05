package com.example.effectivemobiletesttask.utils

import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.models.ResponseData
import com.google.gson.Gson

fun getImageById(itemId: String): List<Int> {
    return when (itemId) {
        "cbf0c984-7c6c-4ada-82da-e29dc698bb50" -> {
            arrayListOf(R.drawable.img_6, R.drawable.img_5)
        }

        "54a876a5-2205-48ba-9498-cfecff4baa6e" -> {
            arrayListOf(R.drawable.img_1, R.drawable.img_2)
        }

        "75c84407-52e1-4cce-a73a-ff2d3ac031b3" -> {
            arrayListOf(R.drawable.img_5, R.drawable.img_6)
        }

        "16f88865-ae74-4b7c-9d85-b68334bb97db" -> {
            arrayListOf(R.drawable.img_3, R.drawable.img_4)
        }

        "26f88856-ae74-4b7c-9d85-b68334bb97db" -> {
            arrayListOf(R.drawable.img_2, R.drawable.img_3)
        }

        "15f88865-ae74-4b7c-9d81-b78334bb97db" -> {
            arrayListOf(R.drawable.img_6, R.drawable.img_1)
        }

        "88f88865-ae74-4b7c-9d81-b78334bb97db" -> {
            arrayListOf(R.drawable.img_4, R.drawable.img_3)
        }

        "55f58865-ae74-4b7c-9d81-b78334bb97db" -> {
            arrayListOf(R.drawable.img_1, R.drawable.img_5)
        }

        else -> emptyList()
    }
}

fun parseJson(jsonString: String) : ResponseData {
    return Gson().fromJson(jsonString, ResponseData::class.java)
}