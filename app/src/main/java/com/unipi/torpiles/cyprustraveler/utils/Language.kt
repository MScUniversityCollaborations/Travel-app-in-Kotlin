package com.unipi.torpiles.cyprustraveler.utils

import Constants.EL
import Constants.EN
import Constants.ENGLISH_LANG
import Constants.GREEK_LANG
import Constants.LANGUAGE
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.unipi.torpiles.cyprustraveler.ui.activities.BaseActivity
import java.util.*

class SetLanguage : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        println("LANGUAGE CLASS: init()")
        setUpLanguage(baseContext)
    }

     fun setUpLanguage(context: Context){
        val sharedPreferences = getDefaultSharedPreferences(context)
        println("LANGUAGE CLASS: setUpLanguage()")
        Log.e("LANGUAGE CLASS", sharedPreferences.getString(LANGUAGE, "").toString())
        when(sharedPreferences.getString(LANGUAGE, "")){
            GREEK_LANG ->    BaseActivity.dLocale = Locale(EL)
            ENGLISH_LANG ->  BaseActivity.dLocale = Locale(EN)
        }

    }

}