package com.example.effectivemobiletesttask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.databinding.FragmentProfileBinding
import com.example.effectivemobiletesttask.vm.FavoriteViewModel
import com.example.effectivemobiletesttask.vm.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            profileViewModel.userLiveData.observe(viewLifecycleOwner) {
                fullName.text = "${it.firstName} ${it.lastName}"
                phoneNumber.text = it.phoneNumber
            }
            if (favoriteViewModel.countSavedItems() == 0) {
                favoritesCount.visibility = View.GONE
                val layoutParams = favorites.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.startToEnd = R.id.favorite_image
                layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                favorites.layoutParams = layoutParams
            } else {
                val layoutParams = favorites.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.bottomToBottom = R.id.favorite_line
                layoutParams.startToEnd = R.id.favorite_image
                layoutParams.topToTop = ConstraintLayout.LayoutParams.UNSET
                favorites.layoutParams = layoutParams
                favoritesCount.visibility = View.VISIBLE
                favoritesCount.text =
                    "${favoriteViewModel.countSavedItems()} ${getWord(favoriteViewModel.countSavedItems())}"
            }
            favoriteBtn.setOnClickListener {
                findNavController().navigate(R.id.favoritesFragment)
            }

            logoutBtn.setOnClickListener {
                profileViewModel.deleteUser()
                favoriteViewModel.clearFavorite()
                findNavController().navigate(R.id.registrationFragment)
            }
        }
    }

    private fun getWord(countSavedItems: Int): String {
        return if (countSavedItems == 1) "товар"
        else "товары"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}