package com.unipi.torpiles.cyprustraveler.ui.activities

import Constants.ENGLISH_LANG
import Constants.GREEK_LANG
import Constants.LANGUAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.orhanobut.hawk.Hawk
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ActivitySettingsBinding
import com.unipi.torpiles.cyprustraveler.utils.SetLanguage
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE


class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        init()
        setupUI()
        setContentView(binding.root)

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

}