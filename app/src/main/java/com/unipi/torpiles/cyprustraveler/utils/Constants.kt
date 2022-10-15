import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.snackbar.BaseTransientBottomBar
import java.text.SimpleDateFormat
import java.util.*

object Constants {

	// General Constants
	const val TAG: String = "[Cyprus Traveler]"
	const val CYPRUS_TRAVELER_PREFERENCES: String = "CyprusTravelerPrefs"
	val STANDARD_SIMPLE_DATE_FORMAT = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
	val SNACKBAR_BEHAVIOR = BaseTransientBottomBar.Behavior().apply {
		setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY) }
	const val DELAY_TIMER_SPLASH_SCREEN: Long = 1500
	const val DELAY_TIMER_SLIDER_SHOW: Long = 3000

	//LANGUAGE
	const val LANGUAGE : String = "language"
	const val ENGLISH_LANG : String =  "English"
	const val GREEK_LANG : String =  "Greek"
	const val EL : String = "el"
	const val EN : String = "en"

	// Firebase Constants
	// Collections
	const val COLLECTION_USERS: String = "users"
	const val COLLECTION_CATEGORIES: String = "categories"
	const val COLLECTION_FAVOURITES: String = "favourites"
	const val COLLECTION_DESTINATIONS: String = "destinations"

	// Fields
	const val FIELD_IN_FAVOURITES: String = "inFavourites"
	const val FIELD_DATE_ADDED: String = "dateAdded"
	const val FIELD_USER_ID: String = "userId"
	const val FIELD_DESTINATION_ID: String = "destinationId"
	const val FIELD_FULL_NAME: String = "fullName"
	const val FIELD_GENDER: String = "gender"
	const val FIELD_IMG_URL: String = "imgUrl"
	const val FIELD_COMPLETE_PROFILE: String = "profileCompleted"
	const val FIELD_PHONE_NUMBER: String = "phoneNumber"
	const val FIELD_PHONE_CODE: String = "phoneCode"
	const val FIELD_ATTRACTIONS: String = "attractions"
	const val FIELD_HOTELS: String = "hotels"
	const val FIELD_RESTAURANTS: String = "restaurant"
	const val FIELD_HAS_SELECTED: String = "hasSelectedInterests"
	const val FIELD_SPORT: String = "sport"
	const val FIELD_RELAXATION: String = "relaxation"
	const val FIELD_ADVENTURE: String = "adventure"

	// Intent Extras
	const val EXTRA_USER_DETAILS: String = "extraUserDetails"
	const val EXTRA_REG_USERS_SNACKBAR: String = "extraShowRegisteredUserSnackbar"
	const val EXTRA_USER_EMAIL: String = "extraUserEmail"
	const val EXTRA_DESTINATION_ID: String = "extraDestinationId"

	// Categories Constants
	const val CATEGORY_BEACH = "beach"
	const val CATEGORY_HOTEL = "hotel"
	const val CATEGORY_CHURCH = "church"
	const val CATEGORY_MONUMENT = "monument"
	const val CATEGORY_BAR = "bar"
	const val CATEGORY_RESTAURANT = "restaurant"
	const val CATEGORY_NATURE = "nature"
	const val CATEGORY_MOUNTAIN = "mountain"

	//A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult in the Base Activity.
	const val READ_STORAGE_PERMISSION_CODE = 2

	// A unique code of image selection from Phone Storage.
	const val PICK_IMAGE_REQUEST_CODE = 2

	/**
	 * A function for user profile image selection from phone storage.
	 */
	fun showImageChooser(activityResultLauncher: ActivityResultLauncher<Intent>) {
		// An intent for launching the image selection of phone storage.
		val galleryIntent = Intent(
			Intent.ACTION_PICK,
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI
		)
		galleryIntent.type = "image/*"
		activityResultLauncher.launch(galleryIntent)
	}

	/**
	 * A function to get the image file extension of the selected image.
	 *
	 * @param activity Activity reference.
	 * @param uri Image file uri.
	 */
	fun getFileExtension(activity: Activity, uri: Uri?): String? {
		/*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
		return MimeTypeMap.getSingleton()
			.getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
	}
}
// END
