package com.unipi.torpiles.cyprustraveler.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unipi.torpiles.cyprustraveler.databinding.ItemTopDestinationBinding
import com.unipi.torpiles.cyprustraveler.models.Destination
import com.unipi.torpiles.cyprustraveler.utils.GlideLoader


/**
 * A adapter class for top destination list items.
 */
open class TopDestinationListAdapter(
    private val context: Context,
    private var list: ArrayList<Destination>
) : RecyclerView.Adapter<TopDestinationListAdapter.TopDestinationsViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link TopDestinationsViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopDestinationsViewHolder {
        return TopDestinationsViewHolder(
            ItemTopDestinationBinding.inflate(
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
    override fun onBindViewHolder(holder: TopDestinationsViewHolder, position: Int) {
        val model = list[position]

        holder.binding.apply {
            GlideLoader(context).loadDestinationPicture(
                model.imgUrl[0],
                imageViewDestination
            )

            when ("Eng") {
                "Eng" -> {
                    textViewDestinationName.text = model.name[0]
                    textViewDestinationQuickDesc.text = model.quickDesc[0]
                }
                "Gr" -> {
                    textViewDestinationName.text = model.name[1]
                    textViewDestinationQuickDesc.text = model.quickDesc[1]
                }
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
    class TopDestinationsViewHolder(val binding: ItemTopDestinationBinding) : RecyclerView.ViewHolder(binding.root)
}
