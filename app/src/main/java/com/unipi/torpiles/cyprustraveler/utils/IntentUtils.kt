package com.unipi.torpiles.cyprustraveler.utils

import android.app.Activity
import android.content.Intent
import com.unipi.torpiles.cyprustraveler.ui.activities.SettingsActivity

class IntentUtils {

    fun goToSettingsActivity(activity: Activity) {
        val intent = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(intent)
    }

}
