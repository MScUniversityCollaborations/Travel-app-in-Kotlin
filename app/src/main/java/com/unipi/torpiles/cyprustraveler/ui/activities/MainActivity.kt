package com.unipi.torpiles.cyprustraveler.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ActivityMainBinding
import com.unipi.torpiles.cyprustraveler.ui.fragments.HomeFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    /*val favouritesFragment = FavouritesFragment()
    val settingsFragment = SettingsFragment()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFragment(homeFragment)
        setupBottomNavigationBar()

    }

    private fun setupBottomNavigationBar() {
        binding.apply {
            /*val radius = resources.getDimension(R.dimen.radius)
            val shapeDrawable : MaterialShapeDrawable= bottomNavigation.background as MaterialShapeDrawable
            shapeDrawable.shapeAppearanceModel = shapeDrawable.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, radius)
                .build()*/

            bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    setCurrentFragment(homeFragment)
                    badgeClear(R.id.nav_home)
                }
                /*R.id.nav_favourites -> {
                    setCurrentFragment(favouritesFragment)
                    badgeClear(R.id.nav_favourites)
                }
                R.id.nav_settings -> {
                    setCurrentFragment(settingsFragment)
                    badgeClear(R.id.nav_settings)
                }*/
            }
            true
        }
        }
    }

    private fun badgeSetup(id: Int, alerts: Int) {
        binding.apply {
            val badge = bottomNavigation.getOrCreateBadge(id)
            badge.isVisible = true
            badge.number = alerts
        }

    }

    private fun badgeClear(id: Int) {
        binding.apply {
            val badgeDrawable = bottomNavigation.getBadge(id)
            if (badgeDrawable != null) {
                badgeDrawable.isVisible = false
                badgeDrawable.clearNumber()
            }
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}
