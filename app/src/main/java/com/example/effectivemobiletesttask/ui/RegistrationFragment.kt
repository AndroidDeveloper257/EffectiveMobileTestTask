package com.example.effectivemobiletesttask.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.database.entity.UserEntity
import com.example.effectivemobiletesttask.databinding.FragmentRegistrationBinding
import com.example.effectivemobiletesttask.vm.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private var isFirstNameValid = false
    private var isLastNameValid = false
    private var isPhoneNumberValid = false

    private var backSpaceClicked = false

    private val TAG = "RegistrationFragment"
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            try {
                profileViewModel.userLiveData.observe(viewLifecycleOwner) {
                    if (it != null) {
                        findNavController().navigate(R.id.homeFragment)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "onAttach: ${e.message}")
            }
            firstNameEt.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    if (isCyrillic(it.toString())) {
                        firstNameHelperTv.visibility = View.GONE
                        isFirstNameValid = true
                        firstNameEt.setBackgroundResource(R.drawable.edit_text_background)
                    } else {
                        firstNameHelperTv.visibility = View.VISIBLE
                        isFirstNameValid = false
                        firstNameEt.setBackgroundResource(R.drawable.error_edit_text_background)
                    }
                    clearFirstName.visibility = View.VISIBLE
                } else {
                    clearFirstName.visibility = View.GONE
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
                        isLastNameValid = false
                        lastNameEt.setBackgroundResource(R.drawable.error_edit_text_background)
                    }
                    clearLastName.visibility = View.VISIBLE
                } else {
                    clearLastName.visibility = View.GONE
                }
                updateLoginBtn()
            }

            phoneNumberEt.addTextChangedListener(object : TextWatcher {
                private var isFormatting = false
                private val phoneNumberFormat = "+ X XXX XXX XX XX"

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (isFormatting) {
                        return
                    }

                    isFormatting = true

                    var digitsOnly = s.toString().replace("\\D".toRegex(), "")
                    if (digitsOnly.length == 1 && !backSpaceClicked && digitsOnly != "7") {
                        digitsOnly = "7$digitsOnly"
                    }
                    Log.d(TAG, "afterTextChanged: $digitsOnly")
                    val formattedPhone = StringBuilder()
                    var index = 0

                    for (i in phoneNumberFormat.indices) {
                        if (index >= digitsOnly.length) {
                            break
                        }

                        if (phoneNumberFormat[i] == 'X') {
                            formattedPhone.append(digitsOnly[index])
                            index++
                        } else {
                            formattedPhone.append(phoneNumberFormat[i])
                        }
                    }
                    phoneNumberEt.setText(formattedPhone.toString())
                    phoneNumberEt.setSelection(formattedPhone.length)
                    if (formattedPhone.isNotEmpty()) {
                        if (formattedPhone.length != 17) {
                            phoneNumberHelperTv.visibility = View.VISIBLE
                            isPhoneNumberValid = false
                            phoneNumberEt.setBackgroundResource(R.drawable.error_edit_text_background)
                        } else {
                            phoneNumberHelperTv.visibility = View.GONE
                            isPhoneNumberValid = true
                            phoneNumberEt.setBackgroundResource(R.drawable.edit_text_background)
                        }
                        clearPhoneNumber.visibility = View.VISIBLE
                    } else {
                        clearPhoneNumber.visibility = View.GONE
                    }
                    isFormatting = false
                    updateLoginBtn()
                }
            })

            phoneNumberEt.setOnKeyListener { view, i, keyEvent ->
                backSpaceClicked = i == KeyEvent.KEYCODE_DEL
                Log.d(TAG, "onViewCreated: $backSpaceClicked")
                false
            }

            clearFirstName.setOnClickListener {
                firstNameEt.setText("")
            }

            clearLastName.setOnClickListener {
                lastNameEt.setText("")
            }

            clearPhoneNumber.setOnClickListener {
                phoneNumberEt.setText("")
            }

            loginBtn.setOnClickListener {
                if (isFormValid()) {
                    profileViewModel.addUser(
                        UserEntity(
                            id = 0,
                            firstName = firstNameEt.text.toString(),
                            lastName = lastNameEt.text.toString(),
                            phoneNumber = phoneNumberEt.text.toString()
                        )
                    )
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        }
    }

    private fun updateLoginBtn() {
        binding.apply {
            if (isFirstNameValid && isLastNameValid && isPhoneNumberValid) {
                Log.d(TAG, "updateLoginBtn: pink color set")
                loginBtn.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.background_pink
                    )
                )
                loginBtn.isFocusable = true
                loginBtn.isClickable = true
                loginBtn.foreground =
                    context?.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackgroundBorderless))
                        ?.getDrawable(0)
            } else {
                Log.d(TAG, "updateLoginBtn: light pink color set")
                loginBtn.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.background_light_pink
                    )
                )
                loginBtn.isClickable = false
            }
        }
    }

    private fun isFormValid(): Boolean {
        return isFirstNameValid && isLastNameValid && isPhoneNumberValid
    }


    private fun isCyrillic(text: String): Boolean {
        return text.all { it.isCyrillic() }
    }

    private fun Char.isCyrillic(): Boolean {
        return this in '\u0400'..'\u04FF' || this == 'ё' || this == 'Ё'
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}