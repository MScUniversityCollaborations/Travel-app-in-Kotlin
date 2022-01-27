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
    val gender: String = "",

    @ServerTimestamp
    val dateRegistered: Date = Date(),
    val profImgUrl: String = "",
    val profileCompleted: Boolean = false,
) : Parcelable
