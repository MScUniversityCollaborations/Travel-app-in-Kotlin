import java.text.SimpleDateFormat
import java.util.*

object Constants {

	// General Constants
	const val TAG: String = "[Cyprus Traveler]"
	const val CYPRUS_TRAVELER_PREFERENCES: String = "CyprusTravelerPrefs"
	val standardSimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)

	// Firebase Constants
	// Collections
	const val COLLECTION_USERS: String = "users"
	const val COLLECTION_CATEGORIES: String = "categories"
	const val COLLECTION_DESTINATIONS: String = "destinations"

	// Fields
	const val FIELD_IN_FAVOURITES: String = "inFavourites"


	// Intent Extras
	const val EXTRA_PRODUCT_ID: String = "extraProductId"
	const val EXTRA_ADDRESS_MODEL: String = "extra_address_model"
	const val EXTRA_ADDRESS_DETAILS: String = "extra_address_details"

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
