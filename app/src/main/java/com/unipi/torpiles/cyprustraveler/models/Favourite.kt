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
data class Favourite(
    var userId: String = "",
    var destinationId: String = "",

    val name: ArrayList<String> = ArrayList(),
    val quickDesc: ArrayList<String> = ArrayList(),
    val category: ArrayList<String> = ArrayList(),
    val imgUrl: ArrayList<String> = ArrayList(),

    @ServerTimestamp
    val dateAdded: Date = Date(),
    var id: String = "",
) : Parcelable
