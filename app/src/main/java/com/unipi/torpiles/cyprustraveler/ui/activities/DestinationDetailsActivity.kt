package com.unipi.torpiles.cyprustraveler.ui.activities

import Constants
import Constants.ENGLISH_LANG
import Constants.GREEK_LANG
import Constants.LANGUAGE
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.orhanobut.hawk.Hawk
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.adapters.SlidingImageAdapter
import com.unipi.torpiles.cyprustraveler.database.FirestoreHelper
import com.unipi.torpiles.cyprustraveler.databinding.ActivityDestinationDetailsBinding
import com.unipi.torpiles.cyprustraveler.models.Destination
import com.unipi.torpiles.cyprustraveler.models.Favourite
import com.unipi.torpiles.cyprustraveler.utils.IntentUtils
import kotlin.math.abs

class DestinationDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityDestinationDetailsBinding
    private lateinit var destinationId: String

    private lateinit var modelDestination: Destination
    private lateinit var modelFavourite: Favourite

    private var isInFavourites: Boolean = false

    private var mHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // This calls the parent constructor
        binding = ActivityDestinationDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view) // This is used to align the xml view to this class

        init()
        setupUI()
    }

    private fun init() {
        if (intent.hasExtra(Constants.EXTRA_DESTINATION_ID)) {
            destinationId =
                intent.getStringExtra(Constants.EXTRA_DESTINATION_ID)!!
        }

        getDestinationDetails()
    }

    private fun getDestinationDetails() {
        showProgressDialog()

        FirestoreHelper().getDestinationDetails(this, destinationId)
    }

    fun successDestinationDetailsFromFirestore(destination: Destination) {
        modelDestination = destination
        loadDestinationDetailsUI()
    }

    private val sliderRunnable = Runnable { binding.viewPagerSlideShow.currentItem += 1 }

    private val viewPagerPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            mHandler.removeCallbacks(sliderRunnable)
            mHandler.postDelayed(sliderRunnable, Constants.DELAY_TIMER_SLIDER_SHOW)
        }
    }

    private fun loadDestinationDetailsUI() {
        binding.apply {
            // View Pager for image slide show
            viewPagerSlideShow.apply {
                adapter = SlidingImageAdapter(
                    this@DestinationDetailsActivity,
                    this,
                    modelDestination.imgUrl
                )
                clipChildren = false
                clipToPadding = false
                getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                val compositePageTransformer = CompositePageTransformer()
                compositePageTransformer.addTransformer(MarginPageTransformer(40))
                compositePageTransformer.addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = 0.85f + r * 0.14f
                }

                setPageTransformer(compositePageTransformer)
                registerOnPageChangeCallback(viewPagerPageChangeCallback)
            }

            Hawk.init(this@DestinationDetailsActivity).build()
            val language : String = Hawk.get(LANGUAGE)
            when (language) {
                ENGLISH_LANG -> {
                    textViewName.text = modelDestination.name[0]
                    textViewQuickDesc.text = modelDestination.quickDesc[0]
                    textViewCategory.text = modelDestination.category[0]
                    textViewDesc.text = modelDestination.desc[0]
                }
                GREEK_LANG -> {
                    textViewName.text = modelDestination.name[1]
                    textViewQuickDesc.text = modelDestination.quickDesc[1]
                    textViewCategory.text = modelDestination.category[1]
                    textViewDesc.text = modelDestination.desc[1]
                }
            }

            when (modelDestination.category[0].lowercase()) {
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

        checkIfDestinationIsInFavourites()
    }

    private fun checkIfDestinationIsInFavourites() {
        FirestoreHelper().getFavouriteDestination(this, modelDestination.id)
    }

    fun successFavouriteDestinationFromFirestore(favourite: Favourite) {
        modelFavourite = favourite

        if (modelFavourite.destinationId != "") {
            isInFavourites = true
            binding.toolbar.actionBarCheckboxFavorite.isChecked = true
        }

        hideProgressDialog()
        unveilDetails()
    }

    private fun unveilDetails() {
        binding.apply {
            vLayoutHead.unVeil()
            vLayoutBody.unVeil()
        }
    }

    private fun setupUI() {
        // Veil the layout until all data is loaded
        binding.apply {
            vLayoutHead.veil()
            vLayoutBody.veil()
        }

        setupActionBar()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // If user IS logged in
        if (FirestoreHelper().getCurrentUserID() != "")
            binding.apply {
                // ActionBar
                toolbar.actionBarCheckboxFavorite.setOnClickListener {
                    if (!toolbar.actionBarCheckboxFavorite.isChecked) {
                        FirestoreHelper().deleteFavourite(
                            this@DestinationDetailsActivity,
                            modelDestination.id
                        )
                    }
                    else {
                        val favorite = Favourite(
                            FirestoreHelper().getCurrentUserID(),
                            modelDestination.id,
                            modelDestination.name,
                            modelDestination.quickDesc,
                            modelDestination.category,
                            modelDestination.imgUrl
                        )
                        FirestoreHelper().addToFavourites(
                            this@DestinationDetailsActivity,
                            favorite
                        )
                    }
                }
            }
        else
            binding.apply {
                listOf(toolbar.actionBarCheckboxFavorite)
                    .forEach {
                        it.setOnClickListener {
                            toolbar.actionBarCheckboxFavorite.isChecked = false
                            IntentUtils().goToSignInActivity(this@DestinationDetailsActivity)
                        }
                    }
            }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar.root)

        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setCustomView(R.layout.toolbar_destination_details)
            it.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorContainer)))
            it.setHomeAsUpIndicator(R.drawable.ic_chevron_left_24dp)
        }
    }

    override fun onResume() {
        super.onResume()

        mHandler.postDelayed(sliderRunnable, Constants.DELAY_TIMER_SLIDER_SHOW)
    }

    override fun onDestroy() {
        super.onDestroy()

        mHandler.removeCallbacks(sliderRunnable)
        binding.viewPagerSlideShow.unregisterOnPageChangeCallback(viewPagerPageChangeCallback)
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(sliderRunnable)
    }
}
