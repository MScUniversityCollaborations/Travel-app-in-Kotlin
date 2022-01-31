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

	// Intent Extras
	const val EXTRA_USER_DETAILS: String = "extraUserDetails"
	const val EXTRA_REG_USERS_SNACKBAR: String = "extraShowRegisteredUserSnackbar"
	const val EXTRA_USER_EMAIL: String = "extraUserEmail"

	// Categories Constants
	const val CATEGORY_BEACH = "beach"
	const val CATEGORY_HOTEL = "hotel"
	const val CATEGORY_CHURCH = "church"
	const val CATEGORY_MONUMENT = "monument"
	const val CATEGORY_BAR = "bar"
	const val CATEGORY_RESTAURANT = "restaurant"
	const val CATEGORY_NATURE = "nature"
	const val CATEGORY_MOUNTAIN = "mountain"
}
// END
