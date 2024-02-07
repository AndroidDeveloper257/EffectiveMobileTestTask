package com.example.effectivemobiletesttask.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.adapter.ProductAdapter
import com.example.effectivemobiletesttask.adapter.TagAdapter
import com.example.effectivemobiletesttask.databinding.FragmentCatalogBinding
import com.example.effectivemobiletesttask.utils.ConsValues.TAG
import com.example.effectivemobiletesttask.utils.SortEnum
import com.example.effectivemobiletesttask.utils.TagsEnum
import com.example.effectivemobiletesttask.vm.ApiStatus
import com.example.effectivemobiletesttask.vm.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()

    private var currentTag: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            val productAdapter = ProductAdapter(productViewModel) {
                val bundle = Bundle()
                bundle.putParcelable("product", it)
                findNavController().navigate(resId = R.id.productInfoFragment, args = bundle)
            }
            productRv.adapter = productAdapter

            lifecycleScope.launch {
                productViewModel.productLiveData.observe(viewLifecycleOwner) {
                    when (it) {
                        is ApiStatus.Error -> {
                            Toast.makeText(requireContext(), "some error", Toast.LENGTH_SHORT)
                                .show()
                            Log.e(TAG, "error: ${it.error}")
                            Log.e(TAG, "error: ${it.error.message}")
                        }

                        is ApiStatus.Loading -> {
                            progress.visibility = View.VISIBLE
                        }

                        is ApiStatus.Success -> {
                            progress.visibility = View.GONE
                            Log.d(TAG, "onViewCreated: ${it.response}")
                            productAdapter.submitList(it.response?.items)
                            productRv.smoothScrollToPosition(0)
                        }
                    }
                }
            }

            val tagAdapter = TagAdapter(
                resources,
                { selectedTag, reselected, position ->
                    tagRv.smoothScrollToPosition(position)
                    if (!reselected) {
                        currentTag = selectedTag
                        if (currentTag == TagsEnum.SEE_ALL.russianTagName) {
                            productAdapter.submitList(productViewModel.getAllProducts(sort.selectedItem.toString()))
                            productRv.smoothScrollToPosition(0)
                        } else {
                            val filteredProducts =
                                productViewModel.getProductsByTag(
                                    getEnglishTagNameByRussian(currentTag.toString()),
                                    sort.selectedItem.toString()
                                )
                            Log.d(
                                TAG,
                                "onViewCreated: ${filteredProducts.size} items got from filter -> $filteredProducts"
                            )
                            productAdapter.submitList(filteredProducts)
                            productRv.smoothScrollToPosition(0)
                        }
                    }
                }, {
                    // clear all filters from productRv
                    currentTag = null
                    productAdapter.submitList(productViewModel.getAllProducts(sort.selectedItem.toString()))
                    productRv.smoothScrollToPosition(0)
                }
            )
            tagRv.adapter = tagAdapter

            val adapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                SortEnum.values().map { it.sortType }
            )
            sort.adapter = adapter
            sort.setSelection(0)

            sort.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (currentTag == null || currentTag == TagsEnum.SEE_ALL.russianTagName) {
                        productAdapter.submitList(productViewModel.getAllProducts(sort.selectedItem.toString()))
                        productRv.smoothScrollToPosition(0)
                    } else {
                        productAdapter.submitList(
                            productViewModel.getProductsByTag(
                                getEnglishTagNameByRussian(currentTag.toString()),
                                sort.selectedItem.toString()
                            )
                        )
                        productRv.smoothScrollToPosition(0)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        }
    }

    private fun getEnglishTagNameByRussian(russianTagName: String): String {
        return TagsEnum.values()
            .find { it.russianTagName == russianTagName }?.englishTagName.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}