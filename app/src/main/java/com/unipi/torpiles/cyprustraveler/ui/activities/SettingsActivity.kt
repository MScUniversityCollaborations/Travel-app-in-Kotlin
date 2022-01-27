package com.unipi.torpiles.cyprustraveler.ui.activities

import Constants.ENGLISH_LANG
import Constants.GREEK_LANG
import Constants.LANGUAGE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ActivitySettingsBinding
import com.unipi.torpiles.cyprustraveler.utils.SetLanguage

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharePrefLagnuage: SharedPreferences
    private lateinit var sharePrefNightMode: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        init()
        setupUI()

        setContentView(binding.root)

    }

    private fun init(){
        sharePrefLagnuage =  PreferenceManager.getDefaultSharedPreferences(this);
        sharePrefNightMode =  PreferenceManager.getDefaultSharedPreferences(this);
    }

    private fun setupUI() {
        setSettings()
        setLocale()
        setupClickListeners()

    }

    private fun setupClickListeners() {
      binding.apply {
            switchNightMode.setOnCheckedChangeListener { _, _ ->
                if (switchNightMode.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    with(sharePrefNightMode.edit()) {
                        //putBoolean(NIGHTMODE, true)
                        apply()
                    }
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    with(sharePrefNightMode.edit()) {
                        //putBoolean(NIGHTMODE, false)
                        apply()
                    }
                }
            }


        radioButtonEnglish.setOnClickListener{
            Log.e("Settings Activity","Radio Group En")
            radioButtonGreek.isClickable = true
            radioButtonEnglish.isClickable = false
           // reloadApp()
        }
        radioButtonGreek.setOnClickListener {
            Log.e("Settings Activity", "Radio Group El")
            radioButtonGreek.isClickable = false
            radioButtonEnglish.isClickable = true
            //reloadApp()
        }
    }
    }

    private fun setLocale(){

            binding.radioGroupLag.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {

                    R.id.radioButtonGreek->{
                        Log.e("Settings Activity", GREEK_LANG)
                        with(sharePrefLagnuage.edit()) {
                            putString(LANGUAGE, GREEK_LANG)
                            apply()
                        }

                    }

                    R.id.radioButtonEnglish -> {
                        Log.e("Settings Activity", ENGLISH_LANG)
                        with(sharePrefLagnuage.edit()) {
                            putString(LANGUAGE, ENGLISH_LANG)
                            apply()
                        }
                    }

                }
                SetLanguage()
            }
    }

    private fun setSettings(){
        when(sharePrefLagnuage.getString(LANGUAGE,"")){
            GREEK_LANG-> binding.radioGroupLag.check(R.id.radioButtonGreek)
            ENGLISH_LANG->  binding.radioGroupLag.check(R.id.radioButtonEnglish)
        }
    }

}