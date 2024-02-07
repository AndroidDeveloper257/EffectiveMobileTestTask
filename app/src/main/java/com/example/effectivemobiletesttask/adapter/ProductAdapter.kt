package com.example.effectivemobiletesttask.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.databinding.ProductItemBinding
import com.example.effectivemobiletesttask.models.Item
import com.example.effectivemobiletesttask.utils.getImageById
import com.example.effectivemobiletesttask.vm.ProductViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ProductAdapter(
    private val productViewModel: ProductViewModel,
    private val onProductClick: (Item) -> Unit
) : ListAdapter<Item, ProductAdapter.ProductViewHolder>(ProductDiffCallBack()) {

    inner class ProductViewHolder(private val productItemBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(productItemBinding.root) {
        fun onBind(product: Item) {
            productItemBinding.apply {
                title.text = product.title
                priceLayout.priceTv.text = "${product.price.price} ${product.price.unit}"
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

                if (productViewModel.isItemSaved(product.id)) {
                    favoriteIv.setImageResource(R.drawable.ic_favorite_selected)
                } else {
                    favoriteIv.setImageResource(R.drawable.ic_favorite_unselected)
                }

                favoriteIv.setOnClickListener {
                    if (productViewModel.isItemSaved(product.id)) {
                        productViewModel.unSaveItem(product.id)
                        favoriteIv.setImageResource(R.drawable.ic_favorite_unselected)
                    } else {
                        productViewModel.saveItem(product.id)
                        favoriteIv.setImageResource(R.drawable.ic_favorite_selected)
                    }
                }

                val imageAdapter = ImageAdapter(getImageById(product.id)) {
                    onProductClick.invoke(product)
                }
                productImagePager.adapter = imageAdapter
                TabLayoutMediator(
                    dotTabLayout,
                    productImagePager
                ) { _, _ ->
                }.attach()

                root.setOnClickListener {
                    onProductClick.invoke(product)
                }
            }
        }
    }

    private class ProductDiffCallBack : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}