package com.unipi.torpiles.cyprustraveler.ui.activities

import Constants.EL
import Constants.EN
import Constants.ENGLISH_LANG
import Constants.GREEK_LANG
import Constants.LANGUAGE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ActivitySettingsBinding
import java.util.*


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
        setLocale()
        setupClickListeners()
        setSettings()
    }

    private fun setupClickListeners() {
        binding.apply {
            radioButtonEnglish.setOnClickListener {
                Log.e("Settings Activity", "Radio Group En")
                radioButtonGreek.isClickable = true
                radioButtonEnglish.isClickable = false
                //finish()
                //recreate()
                //startActivity(intent)
                // reloadApp()
            }
            radioButtonGreek.setOnClickListener {
                Log.e("Settings Activity", "Radio Group El")
                radioButtonGreek.isContextClickable = false
                radioButtonEnglish.isClickable = true
               // finish()
                //recreate()
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
                        dLocale = Locale(EL)

                    }

                    R.id.radioButtonEnglish -> {
                        Log.e("Settings Activity", ENGLISH_LANG)
                        with(sharePrefLagnuage.edit()) {
                            putString(LANGUAGE, ENGLISH_LANG)
                            apply()
                        }
                        dLocale = Locale(EN)

                    }

                }

            }
    }

    private fun setSettings(){
        when(sharePrefLagnuage.getString(LANGUAGE,"")){
            GREEK_LANG-> binding.radioGroupLag.check(R.id.radioButtonGreek)
            ENGLISH_LANG->  binding.radioGroupLag.check(R.id.radioButtonEnglish)
        }
    }

}