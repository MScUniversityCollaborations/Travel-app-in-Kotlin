package com.unipi.torpiles.cyprustraveler.ui.fragments

import Constants.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.unipi.torpiles.cyprustraveler.adapters.DestinationListAdapter
import com.unipi.torpiles.cyprustraveler.adapters.TopDestinationListAdapter
import com.unipi.torpiles.cyprustraveler.database.FirestoreHelper
import com.unipi.torpiles.cyprustraveler.databinding.FragmentHomeBinding
import com.unipi.torpiles.cyprustraveler.models.Destination
import com.unipi.torpiles.cyprustraveler.models.User


class HomeFragment : BaseFragment() {

    // ~~~~~~~VARIABLES~~~~~~~
    private var _binding: FragmentHomeBinding? = null  // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private val binding get() = _binding!!

    // Instance of User data model class. We will initialize it later on.
    private lateinit var mUserDetails: User


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun init() {
        getUserDetails()
        loadDestinations()
        loadTopDestinations()

        setupClickListeners()
    }

    private fun loadDestinations() {
        FirestoreHelper().getDestinationsList(this)
    }

    private fun getUserDetails() {
        FirestoreHelper().getUserDetails(this)
    }

    fun loadUserDetails(userDetails: User) {
        mUserDetails = userDetails
    }


    fun successDestinationsListFromFireStore(destinationsList: ArrayList<Destination>) {

        val destInterestsList: ArrayList<Destination> = ArrayList()

        if (destinationsList.size > 0 && mUserDetails.hasSelectedInterests) {
//            binding.veilRecyclerViewDestinations.visibility = View.VISIBLE
//            binding.layoutUserHasNotSelectedInterests.root.visibility = View.GONE

            destinationsList.forEach{

                if(mUserDetails.adventure == 0){
                    if(it.category[0] == "Mountain" || it.category[0] == "Nature") destInterestsList.add(it)
                }

                if(mUserDetails.hotels == 0){
                    if(it.category[0] == "Hotel") destInterestsList.add(it)
                }

                if(mUserDetails.restaurants == 0){
                    if(it.category[0] == "Restaurant") destInterestsList.add(it)
                }

                if(mUserDetails.relaxation == 0){
                    if(it.category[0] == "Beach" || it.category[0] == "Bar") destInterestsList.add(it)
                }

                if(mUserDetails.attractions == 0){
                    if(it.category[0] == "Church" || it.category[0] == "Monument") destInterestsList.add(it)
                }
            }

            // Show the recycler and remove the empty state layout completely.
            binding.apply {
                veilRecyclerViewDestinations.visibility = View.VISIBLE
            }

            // Sets VeilRecyclerView's properties
            binding.veilRecyclerViewDestinations.run {
                layoutManager = LinearLayoutManager(this@HomeFragment.context, LinearLayoutManager.HORIZONTAL ,false)
                adapter =
                    DestinationListAdapter(
                    requireContext(),
                    destInterestsList
                )
                setHasFixedSize(true)
            }
        } else {
//            binding.apply {
//                veilRecyclerViewDestinations.visibility = View.GONE
//                layoutUserHasNotSelectedInterests.root.visibility = View.VISIBLE
//            }
        }
    }

    private fun loadTopDestinations() {
        FirestoreHelper().getTopDestinationsList(this@HomeFragment)
    }

    fun successTopDestinationsListFromFireStore(topDestinationsList: ArrayList<Destination>) {

        if (topDestinationsList.size > 0) {

            // Show the recycler and remove the empty state layout completely.
            binding.apply {
                veilRecyclerViewTopDestinations.visibility = View.VISIBLE
                layoutEmptyStateTopDestinations.root.visibility = View.GONE
            }

            // Sets VeilRecyclerView's properties
            binding.veilRecyclerViewTopDestinations.run {
                layoutManager = LinearLayoutManager(this@HomeFragment.context, LinearLayoutManager.HORIZONTAL ,false)
                adapter =
                    TopDestinationListAdapter(
                        requireContext(),
                        topDestinationsList
                    )
                setHasFixedSize(true)
            }
        } else {
            // Hide the recycler and show the empty state layout.
            binding.apply {
                veilRecyclerViewTopDestinations.visibility = View.INVISIBLE
                layoutEmptyStateTopDestinations.root.visibility = View.VISIBLE
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            // txtViewTopDestinationsViewAll.setOnClickListener { IntentUtils().goToListProductsActivity(requireActivity(), "Deals") }
        }
    }

    override fun onResume() {
        super.onResume()

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
