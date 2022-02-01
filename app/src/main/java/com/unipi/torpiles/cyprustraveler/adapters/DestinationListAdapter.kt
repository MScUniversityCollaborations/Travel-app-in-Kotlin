package com.unipi.torpiles.cyprustraveler.adapters

import Constants
import Constants.ENGLISH_LANG
import Constants.GREEK_LANG
import Constants.LANGUAGE
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.hawk.Hawk
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ItemDestinationBinding
import com.unipi.torpiles.cyprustraveler.models.Destination
import com.unipi.torpiles.cyprustraveler.utils.GlideLoader
import com.unipi.torpiles.cyprustraveler.utils.IntentUtils


/**
 * A adapter class for destination list items.
 */
open class DestinationListAdapter(
    private val context: Context,
    private var list: ArrayList<Destination>,
) : RecyclerView.Adapter<DestinationListAdapter.DestinationsViewHolder>() {
    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link DestinationsViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationsViewHolder {
        return DestinationsViewHolder(
            ItemDestinationBinding.inflate(
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
    override fun onBindViewHolder(holder: DestinationsViewHolder, position: Int) {
        val model = list[position]

        holder.binding.apply {
            GlideLoader(context).loadDestinationPicture(
                model.imgUrl[0],
                imageViewDestination
            )

            Hawk.init(context).build()
            val language : String = Hawk.get(LANGUAGE)
            when (language) {
                ENGLISH_LANG -> {
                    textViewDestinationName.text = model.name[0]
                    textViewDestinationCategory.text = model.category[0]
                }
                GREEK_LANG -> {
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
            IntentUtils().goToDestinationDetailsActivity(context, model.id)
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
    class DestinationsViewHolder(val binding: ItemDestinationBinding) : RecyclerView.ViewHolder(binding.root)
}
