package com.unipi.torpiles.cyprustraveler.ui.fragments;

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.adapters.FavouritesListAdapter
import com.unipi.torpiles.cyprustraveler.database.FirestoreHelper
import com.unipi.torpiles.cyprustraveler.databinding.FragmentFavouritesBinding
import com.unipi.torpiles.cyprustraveler.models.Favourite

class FavouritesFragment : BaseFragment() {
    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        veilRecycler()

        loadFavorites()
    }

    private fun loadFavorites() {
        FirestoreHelper().getFavouritesList(this@FavouritesFragment)
    }
    /**
     * A function to get the successful product list from cloud firestore.
     *
     * @param favoritesList Will receive the product list from cloud firestore.
     */
    fun successFavouritesListFromFireStore(favoritesList: ArrayList<Favourite>) {

        if (favoritesList.size > 0) {
            binding.veilRecyclerView.visibility = View.VISIBLE
            binding.layoutEmptyState.root.visibility = View.GONE

            // sets VeilRecyclerView's properties
            binding.veilRecyclerView.run {
                setVeilLayout(R.layout.layout_shimmer_item_favourite_destination)
                setAdapter(
                    FavouritesListAdapter(
                        requireActivity(),
                        favoritesList
                    )
                )
                setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false))
                getRecyclerView().setHasFixedSize(true)
                addVeiledItems(5)
                // delay-auto-unveil
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        unVeil()
                    },
                    1000
                )
            }
        }
        else {
            binding.apply {
                veilRecyclerView.unVeil()
                veilRecyclerView.visibility = View.GONE
                layoutEmptyState.root.visibility = View.VISIBLE
            }
        }
    }

    private fun veilRecycler() {
        binding.apply {
            veilRecyclerView.veil()

            veilRecyclerView.addVeiledItems(5)
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
