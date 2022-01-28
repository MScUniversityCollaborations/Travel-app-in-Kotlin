package com.unipi.torpiles.cyprustraveler.utils

import Constants
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.unipi.torpiles.cyprustraveler.models.User
import com.unipi.torpiles.cyprustraveler.ui.activities.*

class IntentUtils {

    fun goToMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goToSettingsActivity(activity: Activity) {
        val intent = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(intent)
    }

    fun goToUpdateUserDetailsActivity(context: Context, user: User) {
        val intent = Intent(context, EditProfileActivity::class.java)
        intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.applicationContext.startActivity(intent)
    }

    fun goToSignInActivity(context: Context) {
        context.startActivity(Intent(context, SignInActivity::class.java))
    }

    fun goToSignUpActivity(context: Context) {
        context.startActivity(Intent(context, SignUpActivity::class.java))
    }
}
