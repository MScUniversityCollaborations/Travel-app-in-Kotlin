package com.unipi.torpiles.cyprustraveler.ui.activities

import Constants.ENGLISH_LANG
import Constants.LANGUAGE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.hawk.Hawk
import com.unipi.torpiles.cyprustraveler.databinding.ActivitySplashBinding
import com.unipi.torpiles.cyprustraveler.utils.SetLanguage
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class SplashActivity : AppCompatActivity() {

    // ~~~~~~~~ VARIABLES ~~~~~~~~
    private lateinit var binding: ActivitySplashBinding
    companion object {
        const val DELAY_TIMER: Long = 1500
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        Hawk.init(this).build()
        checkForHawkKey()
        SetLanguage().setUpLanguage(baseContext)
        hideSystemUI() // Hides the status bar and title from android UI.
        moveToNextActivity() // Moves to next activity in a specific amount of time after loading.
    }

    private fun checkForHawkKey(){
        if(Hawk.contains(LANGUAGE)){
            return
        }else{
            Hawk.put(LANGUAGE, ENGLISH_LANG)
        }
    }


    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun moveToNextActivity() {
        // Create an executor that executes tasks in a background thread.
        val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        // Execute a task in the background thread after some seconds.
        backgroundExecutor.schedule({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // Closing the current activity so the user can't go back to it with the back button and also there is no reason to keep it open.
        }, DELAY_TIMER, java.util.concurrent.TimeUnit.MILLISECONDS)
    }
}
