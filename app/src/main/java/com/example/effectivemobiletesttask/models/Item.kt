package com.example.effectivemobiletesttask.models

import android.os.Parcel
import android.os.Parcelable

data class Item(
    val available: Int,
    val description: String,
    val feedback: Feedback,
    val id: String,
    val info: List<Info>,
    val ingredients: String,
    val price: Price,
    val subtitle: String,
    val tags: List<String>,
    val title: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        TODO("feedback"),
        parcel.readString().toString(),
        TODO("info"),
        parcel.readString().toString(),
        TODO("price"),
        parcel.readString().toString(),
        parcel.createStringArrayList()!!,
        parcel.readString().toString()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}