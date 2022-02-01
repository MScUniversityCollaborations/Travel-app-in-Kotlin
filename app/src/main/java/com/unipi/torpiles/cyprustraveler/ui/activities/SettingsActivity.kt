package com.unipi.torpiles.cyprustraveler.ui.activities

import Constants.ENGLISH_LANG
import Constants.GREEK_LANG
import Constants.LANGUAGE
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.orhanobut.hawk.Hawk
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ActivitySettingsBinding
import com.unipi.torpiles.cyprustraveler.utils.SetLanguage


class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
        setupUI()

    }

    private fun init(){
        Hawk.init(this).build()
    }

    private fun setupUI() {
        checkSystemTheme()
        setLocale()
        setThemeMode()
        setupClickListeners()
        setSettings()
        setupActionBar()
    }

    private fun setupClickListeners() {
        binding.apply {
            radioButtonEnglish.setOnClickListener {
                Log.e("Settings Activity", "Radio Group En")
                radioButtonGreek.isClickable = true
                radioButtonEnglish.isClickable = false
                finish()
                reloadApp()
            }
            radioButtonGreek.setOnClickListener {
                Log.e("Settings Activity", "Radio Group El")
                radioButtonGreek.isClickable = false
                radioButtonEnglish.isClickable = true
                finish()
                reloadApp()
            }
        }
    }

    private fun setLocale(){
        binding.radioGroupLag.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonGreek->{
                    //Log.e("Settings Activity", GREEK_LANG)
                    Hawk.put(LANGUAGE, GREEK_LANG)
                }

                R.id.radioButtonEnglish -> {
                    //Log.e("Settings Activity", ENGLISH_LANG)
                    Hawk.put(LANGUAGE, ENGLISH_LANG)
                }
            }
            SetLanguage().setUpLanguage(baseContext)
        }
    }

    private fun Context.isDarkTheme(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
    }

    private fun setThemeMode(){

//        binding.switchNightMode.setOnCheckedChangeListener { _, _ ->
//                if (binding.switchNightMode.isChecked) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                }
//            }
    }

    private fun checkSystemTheme() {

//        Log.e("Settings Activity","Dark Theme: " + isDarkTheme())
//        when (isDarkTheme()) {
//            false -> {
//                // Night mode is not active, we're using the light theme
//                binding.switchNightMode.isChecked = false
//            }
//            true -> {
//                // Night mode is active, we're using dark theme
//                binding.switchNightMode.isChecked = true
//            }
//        }
    }


    private fun setSettings(){
        val language : String = Hawk.get(LANGUAGE)
        when(language){
            GREEK_LANG-> binding.radioGroupLag.check(R.id.radioButtonGreek)
            ENGLISH_LANG->  binding.radioGroupLag.check(R.id.radioButtonEnglish)
        }
    }

    private fun reloadApp(){
        val i = baseContext.packageManager
            .getLaunchIntentForPackage(baseContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar.root)

        val actionBar = supportActionBar

        actionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorContainer)))
            it.setHomeAsUpIndicator(R.drawable.ic_chevron_left_24dp)
        }
    }
}
