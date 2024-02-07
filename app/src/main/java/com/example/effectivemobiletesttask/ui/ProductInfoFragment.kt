package com.example.effectivemobiletesttask.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.adapter.CharacteristicsAdapter
import com.example.effectivemobiletesttask.adapter.ImageAdapter
import com.example.effectivemobiletesttask.databinding.FragmentProductInfoBinding
import com.example.effectivemobiletesttask.models.Item
import com.example.effectivemobiletesttask.utils.getImageById
import com.example.effectivemobiletesttask.vm.ProductViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductInfoFragment : Fragment() {

    private var _binding: FragmentProductInfoBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()

    private var productId: String? = null
    private var product: Item? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            product = arguments?.getParcelable("product")
            productId = product?.id
            back.setOnClickListener {
                findNavController().popBackStack()
            }
            if (productViewModel.isItemSaved(productId.toString())) {
                favoriteIv.setImageResource(R.drawable.ic_favorite_selected)
            } else {
                favoriteIv.setImageResource(R.drawable.ic_favorite_unselected)
            }
            favoriteIv.setOnClickListener {
                if (productViewModel.isItemSaved(productId.toString())) {
                    productViewModel.unSaveItem(productId.toString())
                    favoriteIv.setImageResource(R.drawable.ic_favorite_unselected)
                } else {
                    productViewModel.saveItem(productId.toString())
                    favoriteIv.setImageResource(R.drawable.ic_favorite_selected)
                }
            }

            val imageAdapter = ImageAdapter(getImageById(productId.toString())) {}
            productImagePager.adapter = imageAdapter
            TabLayoutMediator(
                dotTabLayout,
                productImagePager
            ) { _, _ ->
            }.attach()

            title.text = product?.title
            subtitle.text = product?.subtitle
            available.text = "${resources.getString(R.string.available_1)} ${product?.available} ${
                resources.getString(R.string.available_2)
            }"
            ratingBar.rating = product?.feedback?.rating!!.toFloat()
            ratingTv.text = product?.feedback?.rating.toString()
            reviewCount.text =
                "${product?.feedback!!.count} ${getCountWord(product?.feedback?.count)}"
            priceWithDiscountTv.text =
                "${product?.price?.priceWithDiscount} ${product?.price?.unit}"
            priceWithDiscountTvBottom.text =
                "${product?.price?.priceWithDiscount} ${product?.price?.unit}"
            priceLayout.priceTv.text = "${product?.price?.price} ${product?.price?.unit}"
            priceLayoutBottom.priceTv.text = "${product?.price?.price} ${product?.price?.unit}"
            priceLayoutBottom.priceTv.setTextColor(resources.getColor(R.color.text_light_pink))
            discount.text = "${product?.price?.discount}%"
            brandTv.text = product?.title
            productDescription.text = product?.description
            hideShowProductDescription.setOnClickListener {
                if (productDescription.lineCount > 2) {
                    hideShowProductDescription.text = resources.getString(R.string.more)
                    productDescription.ellipsize = TextUtils.TruncateAt.END
                    productDescription.maxLines = 2
                    brandBtn.visibility = View.GONE
                } else {
                    hideShowProductDescription.text = resources.getString(R.string.hide)
                    productDescription.ellipsize = null
                    productDescription.maxLines = Int.MAX_VALUE
                    brandBtn.visibility = View.VISIBLE
                }
            }
            characteristicsRv.adapter = CharacteristicsAdapter(product?.info!!)
            ingredients.text = product?.ingredients
            hideShowIngredients.setOnClickListener {
                if (ingredients.lineCount > 2) {
                    hideShowIngredients.text = resources.getString(R.string.more)
                    ingredients.ellipsize = TextUtils.TruncateAt.END
                    ingredients.maxLines = 2
                } else {
                    hideShowIngredients.text = resources.getString(R.string.hide)
                    ingredients.ellipsize = null
                    ingredients.maxLines = Int.MAX_VALUE
                }
            }
        }
    }

    private fun getCountWord(count: Int?): String {
        return when (count) {
            1 -> "отзыв"
            in 2..4 -> "отзыва"
            else -> "отзывов"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}