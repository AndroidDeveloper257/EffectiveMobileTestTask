package com.example.effectivemobiletesttask.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.effectivemobiletesttask.adapter.ProductAdapter
import com.example.effectivemobiletesttask.databinding.FragmentCatalogBinding
import com.example.effectivemobiletesttask.models.ResponseData
import com.example.effectivemobiletesttask.utils.ConsValues.TAG
import com.example.effectivemobiletesttask.vm.ApiStatus
import com.example.effectivemobiletesttask.vm.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            val adapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                arrayOf("item", "book", "laptop")
            )
            sort.adapter = adapter

            lifecycleScope.launch {
                productViewModel.productLiveData.observe(viewLifecycleOwner) {
                    when (it) {
                        is ApiStatus.Error -> {
                            error(it.error)
                        }

                        is ApiStatus.Loading -> {
                            loading()
                        }

                        is ApiStatus.Success -> {

                            success(it.response!!)
                        }
                    }
                }
            }
        }
    }

    private fun error(error: Throwable) {
        Toast.makeText(requireContext(), "some error", Toast.LENGTH_SHORT).show()
        Log.e(TAG, "error: $error")
        Log.e(TAG, "error: ${error.message}")
    }

    private fun loading() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun success(responseData: ResponseData) {
        binding.apply {
            progress.visibility = View.GONE
            val productAdapter = ProductAdapter(responseData.items)
            productRv.adapter = productAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}