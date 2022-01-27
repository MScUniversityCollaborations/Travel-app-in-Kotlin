package com.unipi.torpiles.cyprustraveler.ui.fragments;

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.adapters.DestinationListAdapter
import com.unipi.torpiles.cyprustraveler.adapters.TopDestinationListAdapter
import com.unipi.torpiles.cyprustraveler.database.FirestoreHelper
import com.unipi.torpiles.cyprustraveler.databinding.FragmentHomeBinding
import com.unipi.torpiles.cyprustraveler.models.Destination

class HomeFragment : BaseFragment() {

    // ~~~~~~~VARIABLES~~~~~~~
    private var _binding: FragmentHomeBinding? = null  // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private val binding get() = _binding!!
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        veilRecyclers()
        loadDestinations()
        loadTopDestinations()
        setupClickListeners()
    }

    private fun loadDestinations() {
        FirestoreHelper().getDestinationsList(this@HomeFragment)
    }

    fun successDestinationsListFromFireStore(destinationsList: ArrayList<Destination>) {

        if (destinationsList.size > 0) {

            // Show the recycler and remove the empty state layout completely.
            binding.apply {
                veilRecyclerViewDestinations.visibility = View.VISIBLE
            }

            // Sets VeilRecyclerView's properties
            binding.veilRecyclerViewDestinations.run {
                setVeilLayout(R.layout.layout_shimmer_item_destination)
                setAdapter(
                    DestinationListAdapter(
                        requireContext(),
                        destinationsList
                    )
                )
                setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false))
                getRecyclerView().setHasFixedSize(true)
                addVeiledItems(3)
                // Delay-auto-unveil
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        unveilRecyclers()
                    },
                    1000
                )
            }
        }
        else
            unveilRecyclers()
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
                setVeilLayout(R.layout.layout_shimmer_item_top_destination)
                setAdapter(
                    TopDestinationListAdapter(
                        requireContext(),
                        topDestinationsList
                    )
                )
                setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false))
                getRecyclerView().setHasFixedSize(true)
                addVeiledItems(5)
                // Delay-auto-unveil
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        unveilRecyclers()
                    },
                    1000
                )
            }
        } else {
            unveilRecyclers()
            // Hide the recycler and show the empty state layout.
            binding.apply {
                veilRecyclerViewTopDestinations.visibility = View.INVISIBLE
                layoutEmptyStateTopDestinations.root.visibility = View.VISIBLE
            }

        }
    }

    private fun veilRecyclers() {
        binding.apply {
            veilRecyclerViewTopDestinations.veil()
            veilRecyclerViewDestinations.veil()

            veilRecyclerViewTopDestinations.addVeiledItems(5)
            veilRecyclerViewDestinations.addVeiledItems(3)
        }
    }

    private fun unveilRecyclers() {
        binding.apply {
            veilRecyclerViewTopDestinations.unVeil()
            veilRecyclerViewDestinations.unVeil()
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
