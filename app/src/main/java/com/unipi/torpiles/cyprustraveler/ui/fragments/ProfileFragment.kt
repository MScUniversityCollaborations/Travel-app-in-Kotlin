package com.unipi.torpiles.cyprustraveler.ui.fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unipi.torpiles.cyprustraveler.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment() {

    // ~~~~~~~VARIABLES~~~~~~~
    private var _binding: FragmentProfileBinding? = null  // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private val binding get() = _binding!!
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        // setupClickListeners()
    }


    /*private fun setupClickListeners() {
        binding.apply {
            txtViewCategoriesViewAll.setOnClickListener { IntentUtils().goToCategoriesActivity(requireActivity()) }
            txtViewDealsViewAll.setOnClickListener { IntentUtils().goToListProductsActivity(requireActivity(), "Deals") }
            txtViewPopularViewAll.setOnClickListener { IntentUtils().goToListProductsActivity(requireActivity(), "Popular") }
            imgBtnCategoryChilled.setOnClickListener{ IntentUtils().goToListProductsActivity(requireActivity(), "Chilled") }
            imgBtnCategoryGrocery.setOnClickListener{ IntentUtils().goToListProductsActivity(requireActivity(), "Grocery") }
            imgBtnCategoryHousehold.setOnClickListener{ IntentUtils().goToListProductsActivity(requireActivity(), "Household") }
            imgBtnCategoryLiquor.setOnClickListener{ IntentUtils().goToListProductsActivity(requireActivity(), "Liquor") }
        }
    }*/

    override fun onResume() {
        super.onResume()

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
