package com.unipi.torpiles.cyprustraveler.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.unipi.torpiles.cyprustraveler.databinding.ImageContainerLayoutBinding
import com.unipi.torpiles.cyprustraveler.utils.GlideLoader

class SlidingImageAdapter(
    private val context: Context,
    private val viewPager2: ViewPager2,
    private val imageUrlList: ArrayList<String>
) : RecyclerView.Adapter<SlidingImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ImageContainerLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.apply {
            GlideLoader(context).loadDestinationPictureSlideShow(
                imageUrlList[position],
                imageViewDestination
            )
        }

        if (position == imageUrlList.size - 1) {
            viewPager2.post(mRunnable)
        }
    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ImageViewHolder(val binding: ImageContainerLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val mRunnable = Runnable {
        imageUrlList.addAll(imageUrlList)
        notifyDataSetChanged()
    }
}
