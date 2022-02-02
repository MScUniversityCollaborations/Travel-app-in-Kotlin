package com.unipi.torpiles.cyprustraveler.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ActivityMainBinding
import com.unipi.torpiles.cyprustraveler.ui.fragments.FavouritesFragment
import com.unipi.torpiles.cyprustraveler.ui.fragments.HomeFragment
import com.unipi.torpiles.cyprustraveler.ui.fragments.ProfileFragment
import com.unipi.torpiles.cyprustraveler.utils.IntentUtils

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val favouritesFragment = FavouritesFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        binding.bottomNavigation.selectedItemId = R.id.nav_home
    }

    private fun setupBottomNavBar() {
        binding.apply {
            bottomNavigation.apply {
                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.nav_home -> {
                            replaceFragment(homeFragment)
                            badgeClear(R.id.nav_home)
                        }
                        R.id.nav_favourites -> {
                            replaceFragment(favouritesFragment)
                            badgeClear(R.id.nav_favourites)
                        }
                        R.id.nav_profile -> {
                            replaceFragment(profileFragment)
                            badgeClear(R.id.nav_profile)
                        }
                    }
                    true
                }
                itemIconTintList = null
            }
        }
    }

    private fun setupUI() {
        setupNavDrawer()
        setupBottomNavBar()
    }

    private fun setupNavDrawer() {
        binding.apply {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity, drawerLayout, toolbar.root,
                R.string.nav_drawer_close_res, R.string.nav_drawer_close_res
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.drawerArrowDrawable.barLength = 64F
            toggle.drawerArrowDrawable.barThickness = 9F
            toggle.drawerArrowDrawable.gapSize = 12F

            // Set navigation arrow icon
            toggle.setHomeAsUpIndicator(R.drawable.ic_list_indicator)
            toggle.syncState()

            navView.setNavigationItemSelectedListener(this@MainActivity)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            R.id.nav_drawer_home -> binding.bottomNavigation.selectedItemId = R.id.nav_home
            R.id.nav_drawer_favourites -> binding.bottomNavigation.selectedItemId = R.id.nav_favourites
            R.id.nav_drawer_profile -> binding.bottomNavigation.selectedItemId = R.id.nav_profile
            R.id.nav_drawer_settings -> {
                IntentUtils().goToSettingsActivity(this@MainActivity)
                return false
            }
            R.id.nav_drawer_exit -> ActivityCompat.finishAffinity(this)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_Layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
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

    private inline fun FragmentManager.doTransaction(func: FragmentTransaction.() ->
    FragmentTransaction
    ) {
        beginTransaction().func().commit()
    }

    private fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.doTransaction{replace(frameId, fragment)}
    }


    private fun replaceFragment(fragment: Fragment) {
        if (!fragment.isRemoving)
            replaceFragment(R.id.fl_wrapper, fragment)

        // Title bar title
        binding.apply {
            when (fragment) {
                is HomeFragment -> {
                    toolbar.textViewTitle.text = getString(R.string.txt_discover)
                    navView.setCheckedItem(R.id.nav_drawer_home)
                }
                is FavouritesFragment -> {
                    toolbar.textViewTitle.text = getString(R.string.txt_favourites)
                    navView.setCheckedItem(R.id.nav_drawer_favourites)
                }
                is ProfileFragment -> {
                    toolbar.textViewTitle.text = getString(R.string.txt_profile)
                    navView.setCheckedItem(R.id.nav_drawer_profile)
                }
            }
        }
    }

    /*fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment){
        supportFragmentManager.doTransaction { add(frameId, fragment) }
    }

    fun AppCompatActivity.removeFragment(fragment: Fragment) {
        supportFragmentManager.doTransaction{remove(fragment)}
    }

    private fun badgeSetup(id: Int, alerts: Int) {
        binding.apply {
            val badge = bottomNavigation.getOrCreateBadge(id)
            badge.isVisible = true
            badge.number = alerts
        }
    }*/

    override fun onBackPressed() {
        doubleBackToExit()
    }
}
