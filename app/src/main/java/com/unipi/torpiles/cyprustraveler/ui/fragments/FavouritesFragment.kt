package com.unipi.torpiles.cyprustraveler.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.adapters.FavouritesListAdapter
import com.unipi.torpiles.cyprustraveler.database.FirestoreHelper
import com.unipi.torpiles.cyprustraveler.databinding.FragmentFavouritesBinding
import com.unipi.torpiles.cyprustraveler.models.Favourite
import com.unipi.torpiles.cyprustraveler.utils.IntentUtils

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        // veilRecycler()

        loadFavorites()
    }

    private fun loadFavorites() {
        // Check if the user is logged in, otherwise show the sign in state.
        if (FirestoreHelper().getCurrentUserID() != "") {
            FirestoreHelper().getFavouritesList(this@FavouritesFragment)
        }
        else // If user is not signed in
            binding.apply {
                // We make the sign in layout visible and add the button click listeners accordingly.
                layoutMustBeSignedIn.apply {
                    root.visibility = View.VISIBLE
                    btnSignIn.setOnClickListener{
                        IntentUtils().goToSignInActivity(this@FavouritesFragment.requireContext())
                    }
                    txtViewSignUp.setOnClickListener{
                        IntentUtils().goToSignUpActivity(this@FavouritesFragment.requireContext())
                    }
                }
            }
    }
    /**
     * A function to get the successful favourite list from cloud firestore.
     *
     * @param favouritesList Will receive the favourite list from cloud firestore.
     */
    fun successFavouritesListFromFireStore(favouritesList: ArrayList<Favourite>) {

        if (favouritesList.size > 0) {
            binding.veilRecyclerView.visibility = View.VISIBLE
            binding.layoutEmptyState.root.visibility = View.GONE

            // sets VeilRecyclerView's properties
            binding.veilRecyclerView.run {
                setVeilLayout(R.layout.layout_shimmer_item_favourite_destination)
                setAdapter(
                    FavouritesListAdapter(
                        requireContext(),
                        favouritesList
                    )
                )
                setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false))
                getRecyclerView().setHasFixedSize(true)
                unVeil()

                /*addVeiledItems(5)
                // delay-auto-unveil
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        unVeil()
                    },
                    1000
                )*/
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

        // init()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
