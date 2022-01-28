package com.unipi.torpiles.cyprustraveler.ui.activities

import Constants.ENGLISH_LANG
import Constants.GREEK_LANG
import Constants.LANGUAGE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.orhanobut.hawk.Hawk
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ActivitySettingsBinding
import com.unipi.torpiles.cyprustraveler.utils.SetLanguage
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
        Hawk.init(this).build()
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
                 finish()
                 //recreate()
                 //startActivity(intent)
                 reloadApp()
            }
            radioButtonGreek.setOnClickListener {
                Log.e("Settings Activity", "Radio Group El")
                radioButtonGreek.isContextClickable = false
                radioButtonEnglish.isClickable = true
                 finish()
//                 recreate()
                 reloadApp()
            }
        }
    }

    private fun setLocale(){
            binding.radioGroupLag.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioButtonGreek->{
                        Log.e("Settings Activity", GREEK_LANG)
                        Hawk.put(LANGUAGE, GREEK_LANG);
                   /*     with(sharePrefLagnuage.edit()) {
                            putString(LANGUAGE, GREEK_LANG)
                            apply()
                        }*/
                    }

                    R.id.radioButtonEnglish -> {
                        Log.e("Settings Activity", ENGLISH_LANG)
                        Hawk.put(LANGUAGE, ENGLISH_LANG);
//                        with(sharePrefLagnuage.edit()) {
//                            putString(LANGUAGE, ENGLISH_LANG)
//                            apply()
//                        }

                    }

                }
                SetLanguage().setUpLanguage(baseContext)
            }
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