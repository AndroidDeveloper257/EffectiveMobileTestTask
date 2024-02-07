package com.example.effectivemobiletesttask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.adapter.ProductAdapter
import com.example.effectivemobiletesttask.databinding.FragmentProductsBinding
import com.example.effectivemobiletesttask.vm.ApiStatus
import com.example.effectivemobiletesttask.vm.FavoriteViewModel
import com.example.effectivemobiletesttask.vm.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(layoutInflater)
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

            val savedItems = productViewModel.listSavedItems()

            if (savedItems.isEmpty()) {
                empty.visibility = View.VISIBLE
            } else {
                empty.visibility = View.GONE
            }

            productViewModel.productLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is ApiStatus.Error -> {

                    }
                    is ApiStatus.Loading -> {

                    }
                    is ApiStatus.Success -> {
                        productAdapter.submitList(productViewModel.getSavedItems(savedItems))
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() =
            ProductsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}