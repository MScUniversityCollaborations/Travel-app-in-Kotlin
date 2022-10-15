package com.unipi.torpiles.cyprustraveler.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * A data model class with required fields.
 */

@Keep // Denotes that the annotated element should not be removed when the code is minified at build time.
@Parcelize // A Parcelable implementation is automatically generated.
@IgnoreExtraProperties
data class User(
    val id: String = "",

    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val phoneCode: Int = 0,
    val gender: Int = -1,
    val adventure: Int = 1,
    val sport: Int = 1,
    val relaxation: Int = 1,
    val attractions: Int = 1,
    val hotels : Int = 1,
    val restaurants : Int = 1,
    val hasSelectedInterests: Boolean = false,


    @ServerTimestamp
    val dateRegistered: Date = Date(),
    val imgUrl: String = "",
    val profileCompleted: Boolean = false,
) : Parcelable
