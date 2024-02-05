package com.example.effectivemobiletesttask.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private var isFirstNameValid = false
    private var isLastNameValid = false
    private var isPhoneNumberValid = false

    private val TAG = "RegistrationFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            firstNameEt.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    if (isCyrillic(it.toString())) {
                        firstNameHelperTv.visibility = View.GONE
                        isFirstNameValid = true
                        firstNameEt.setBackgroundResource(R.drawable.edit_text_background)
                    } else {
                        firstNameHelperTv.visibility = View.VISIBLE
                        firstNameEt.setBackgroundResource(R.drawable.error_edit_text_background)
                    }
                }
                updateLoginBtn()
            }
            lastNameEt.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    if (isCyrillic(it.toString())) {
                        lastNameHelperTv.visibility = View.GONE
                        isLastNameValid = true
                        lastNameEt.setBackgroundResource(R.drawable.edit_text_background)
                    } else {
                        lastNameHelperTv.visibility = View.VISIBLE
                        lastNameEt.setBackgroundResource(R.drawable.error_edit_text_background)
                    }
                }
                updateLoginBtn()
            }
            phoneNumberEt.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    if (it.toString().length == 12) {
                        phoneNumberHelperTv.visibility = View.GONE
                        isPhoneNumberValid = true
                        phoneNumberEt.setBackgroundResource(R.drawable.edit_text_background)
                    } else {
                        phoneNumberHelperTv.visibility = View.VISIBLE
                        phoneNumberEt.setBackgroundResource(R.drawable.error_edit_text_background)
                    }
                }
                updateLoginBtn()
            }
            loginBtn.setOnClickListener {
                if (isFormValid()) {
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        }
    }

    private fun updateLoginBtn() {
        binding.apply {
            if (isFirstNameValid && isLastNameValid && isPhoneNumberValid) {
                loginBtn.setCardBackgroundColor(resources.getColor(R.color.background_pink))
                loginBtn.isFocusable = true
                loginBtn.isClickable = true
                loginBtn.foreground =
                    context?.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackgroundBorderless))
                        ?.getDrawable(0)
            } else {
                loginBtn.setCardBackgroundColor(resources.getColor(R.color.background_light_pink))
                loginBtn.isClickable = false
            }
        }
    }

    private fun isFormValid(): Boolean {
        return isFirstNameValid && isLastNameValid && isPhoneNumberValid
    }


    private fun isCyrillic(text: String): Boolean {
        return Regex("\\p{InCyrillic}&&\\S+").matches(text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}