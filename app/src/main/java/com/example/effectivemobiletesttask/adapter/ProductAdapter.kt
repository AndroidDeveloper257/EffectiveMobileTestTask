package com.example.effectivemobiletesttask.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.databinding.ProductItemBinding
import com.example.effectivemobiletesttask.models.Item

class ProductAdapter(
    private val productList: List<Item>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val productItemBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(productItemBinding.root) {
        fun onBind(position: Int) {
            productItemBinding.apply {
                val product = productList[position]
                title.text = product.title
                priceTv.text = "${product.price.price} ${product.price.unit}"
                priceWithDiscountTv.text =
                    "${product.price.priceWithDiscount} ${product.price.unit}"
                discount.text = "${product.price.discount}%"
                subtitle.text = product.subtitle
                try {
                    rating.text = product.feedback.rating.toString()
                    count.text = "(${product.feedback.count})"
                } catch (e: Exception) {
                    infoLayout.visibility = View.GONE
                }
                productImagePager.adapter = ImageAdapter(emptyList())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.onBind(position)
    }

}