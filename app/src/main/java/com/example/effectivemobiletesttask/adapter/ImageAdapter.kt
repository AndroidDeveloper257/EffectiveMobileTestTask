package com.example.effectivemobiletesttask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.effectivemobiletesttask.databinding.ImageItemBinding

class ImageAdapter(
    private val imageResourceList: List<Int>
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){

    inner class ImageViewHolder(private val imageItemBinding: ImageItemBinding) : RecyclerView.ViewHolder(imageItemBinding.root) {
        fun onBind(position: Int) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = imageResourceList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind(position)
    }
}