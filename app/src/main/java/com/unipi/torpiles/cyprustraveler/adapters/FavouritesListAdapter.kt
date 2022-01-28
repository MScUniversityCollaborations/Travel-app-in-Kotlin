package com.unipi.torpiles.cyprustraveler.adapters

import Constants
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ItemFavouriteDestinationBinding
import com.unipi.torpiles.cyprustraveler.models.Favourite
import com.unipi.torpiles.cyprustraveler.utils.GlideLoader


/**
 * A adapter class for products list items.
 */
open class FavouritesListAdapter(
    private val context: Context,
    private var list: ArrayList<Favourite>
) : RecyclerView.Adapter<FavouritesListAdapter.FavouritesViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ProductsViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        return FavouritesViewHolder(
            ItemFavouriteDestinationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val model = list[position]

        holder.binding.apply {
            GlideLoader(context).loadDestinationPicture(
                model.imgUrl[0],
                imageViewDestination
            )

            when ("Eng") {
                "Eng" -> {
                    textViewDestinationName.text = model.name[0]
                    textViewDestinationCategory.text = model.category[0]
                }
                "Gr" -> {
                    textViewDestinationName.text = model.name[1]
                    textViewDestinationCategory.text = model.category[1]
                }
            }

            when (model.category[0].lowercase()) {
                Constants.CATEGORY_BEACH -> imageViewDestinationTypeIcon.setImageResource(R.drawable.ic_beach)
                Constants.CATEGORY_HOTEL -> imageViewDestinationTypeIcon.setImageResource(R.drawable.ic_hotel)
                Constants.CATEGORY_NATURE -> imageViewDestinationTypeIcon.setImageResource(R.drawable.ic_forest)
                Constants.CATEGORY_MOUNTAIN -> imageViewDestinationTypeIcon.setImageResource(R.drawable.ic_mountains)
                Constants.CATEGORY_BAR -> imageViewDestinationTypeIcon.setImageResource(R.drawable.ic_hotel)
                Constants.CATEGORY_RESTAURANT -> imageViewDestinationTypeIcon.setImageResource(R.drawable.ic_hotel)
                Constants.CATEGORY_MONUMENT -> imageViewDestinationTypeIcon.setImageResource(R.drawable.ic_monument)
                Constants.CATEGORY_CHURCH -> imageViewDestinationTypeIcon.setImageResource(R.drawable.ic_church)
            }
        }
        // Click event on item click
        holder.itemView.setOnClickListener {
            // IntentUtils().goToDestinationDetailsActivity(context, model.id)
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class FavouritesViewHolder(val binding: ItemFavouriteDestinationBinding) : RecyclerView.ViewHolder(binding.root)
}
