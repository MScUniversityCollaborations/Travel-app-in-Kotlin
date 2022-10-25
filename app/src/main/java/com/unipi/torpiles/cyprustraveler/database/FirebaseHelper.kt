package com.unipi.torpiles.cyprustraveler.database

import Constants
import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unipi.torpiles.cyprustraveler.models.Destination
import com.unipi.torpiles.cyprustraveler.models.Favourite
import com.unipi.torpiles.cyprustraveler.models.User
import com.unipi.torpiles.cyprustraveler.ui.activities.DestinationDetailsActivity
import com.unipi.torpiles.cyprustraveler.ui.activities.EditProfileActivity
import com.unipi.torpiles.cyprustraveler.ui.activities.SignInActivity
import com.unipi.torpiles.cyprustraveler.ui.activities.SignUpActivity
import com.unipi.torpiles.cyprustraveler.ui.fragments.FavouritesFragment
import com.unipi.torpiles.cyprustraveler.ui.fragments.HomeFragment
import com.unipi.torpiles.cyprustraveler.ui.fragments.ProfileFragment

class FirestoreHelper {

    // Access a Cloud Firestore instance.
    private val dbFirestore = FirebaseFirestore.getInstance()


    /**
     * A function to get the user id of current logged user.
     */
    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    /**
     * A function to add a destination to current logged user's favourites.
     */
    fun addToFavourites(activity: Activity, favourite: Favourite) {
        dbFirestore.collection(Constants.COLLECTION_FAVOURITES)
            .document()
            .set(favourite, SetOptions.merge())
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding a destination to user's favourites.",
                    e)
            }
    }

    /**
     * A function to delete a destination to current logged user's favourites.
     */
    fun deleteFavourite(activity: Activity, destinationId: String) {
        dbFirestore.collection(Constants.COLLECTION_FAVOURITES)
            .whereEqualTo(Constants.FIELD_USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.FIELD_DESTINATION_ID, destinationId)
            .get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        dbFirestore
                            .collection(Constants.COLLECTION_FAVOURITES)
                            .document(document.id)
                            .delete()
                    }
                }
                else {
                    Log.d(activity.javaClass.simpleName,
                        "Error getting documents for deletion: ",
                        task.exception)
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting destination details from user's favourites.",
                    e)
            }
    }

    /**
     * A function to get the destinations list from cloud firestore.
     *
     * @param fragment The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    fun getDestinationsList(fragment: Fragment) {
        dbFirestore.collection(Constants.COLLECTION_DESTINATIONS)
            // .limit(10)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.d("Destinations List", document.documents.toString())

                // Here we have created a new instance for Destinations ArrayList.
                val destinationsList: ArrayList<Destination> = ArrayList()

                // A for loop as per the list of documents to convert them into Destinations ArrayList.
                for (i in document.documents) {

                    val destination = i.toObject(Destination::class.java)
                    destination!!.id = i.id

                    destinationsList.add(destination)
                }
                when (fragment) {
                    is HomeFragment -> {
                        fragment.successDestinationsListFromFireStore(destinationsList)
                    }
                    else -> {}
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is HomeFragment -> {
                        // TODO: Show error state maybe
                    }
                }

                Log.e("Get Destinations List", "Error while getting destination list.", e)
            }
    }

    /**
     * A function to get the top destinations list from cloud firestore by checking how many users
     * have destinations in their favourites.
     *
     * @param fragment The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    fun getTopDestinationsList(fragment: Fragment) {
        dbFirestore.collection(Constants.COLLECTION_DESTINATIONS)
            .whereGreaterThan(Constants.FIELD_IN_FAVOURITES, 0)
            .orderBy(Constants.FIELD_IN_FAVOURITES, Query.Direction.DESCENDING)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.d("Top Destinations List", document.documents.toString())

                // Here we have created a new instance for Destinations ArrayList.
                val topDestinationsList: ArrayList<Destination> = ArrayList()

                // A for loop as per the list of documents to convert them into Destinations ArrayList.
                for (i in document.documents) {

                    val destination = i.toObject(Destination::class.java)
                    destination!!.id = i.id

                    topDestinationsList.add(destination)
                }
                when (fragment) {
                    is HomeFragment -> {
                        fragment.successTopDestinationsListFromFireStore(topDestinationsList)
                    }
                    else -> {}
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is HomeFragment -> {
                        // TODO: Show error state maybe
                    }
                }

                Log.e("Get Top Destinations List", "Error while getting top destination list.", e)
            }
    }

    fun updateProfile(activity: EditProfileActivity, userHashMap: HashMap<String, Any>) {

        dbFirestore.collection(Constants.COLLECTION_USERS)
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userHashMap, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.successUpdateProfileToFirestore()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user's profile.",
                    e
                )
            }
    }

    /**
     * A function to get the logged user details from from FireStore Database.
     */
    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        dbFirestore.collection(Constants.COLLECTION_USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.d(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                when (activity) {
                    is SignInActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    /**
     * A function to get the logged user details from from FireStore Database.
     */
    fun getUserDetails(fragment: Fragment) {

        // Here we pass the collection name from which we wants the data.
        dbFirestore.collection(Constants.COLLECTION_USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.d(fragment.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                when (fragment) {
                    is ProfileFragment -> {
                        fragment.userDetailsSuccess(user)
                    }
                    is HomeFragment ->{
                        fragment.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->

                when (fragment) {
                    is ProfileFragment -> {
                        fragment.unveilDetails()
                        // TODO Show error
                        /*fragment.hideProgressDialog()*/
                    }
                }

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    /**
     * A function to make an entry of the registered user in the FireStore database.
     */
    fun registerUser(activity: SignUpActivity, userInfo: User) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        dbFirestore.collection(Constants.COLLECTION_USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    /**
     * A function to get destination details based on the product id.
     */
    fun getDestinationDetails(activity: Activity, destinationId: String) {
        dbFirestore.collection(Constants.COLLECTION_DESTINATIONS)
            .document(destinationId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                // Here we get destination details in the form of document.
                Log.d(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Destination data model class.
                val destination = document.toObject(Destination::class.java)!!

                when (activity) {
                    is DestinationDetailsActivity -> activity.successDestinationDetailsFromFirestore(destination)
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting destination details.",
                    e
                )
            }
    }

    fun getFavouritesList(fragmentFavourites: FavouritesFragment) {

        dbFirestore.collection(Constants.COLLECTION_FAVOURITES)
            .whereEqualTo(Constants.FIELD_USER_ID, getCurrentUserID())
            .orderBy(Constants.FIELD_DATE_ADDED, Query.Direction.DESCENDING)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.d("Favourites List", document.documents.toString())

                // Here we have created a new instance for Favorites ArrayList.
                val favouritesList: ArrayList<Favourite> = ArrayList()

                // A for loop as per the list of documents to convert them into Favorites ArrayList.
                for (i in document.documents) {

                    val favourite = i.toObject(Favourite::class.java)
                    favourite!!.id = i.id

                    favouritesList.add(favourite)
                }

                fragmentFavourites.successFavouritesListFromFireStore(favouritesList)
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                // TODO: Show error state maybe

                Log.e("Get Favourites List", "Error while getting favourite destination list.", e)
            }
    }

    fun getFavouriteDestination(activity: Activity, destinationId: String) {
        dbFirestore.collection(Constants.COLLECTION_FAVOURITES)
            .whereEqualTo(Constants.FIELD_USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.FIELD_DESTINATION_ID, destinationId)
            .get() // Will get the documents snapshots.
            .addOnCompleteListener { task ->

                if (task.result!!.isEmpty) {
                    when (activity) {
                        // Here we will send an empty class just to handle the destination not in our favourites.
                        is DestinationDetailsActivity -> activity.successFavouriteDestinationFromFirestore(Favourite())
                    }
                    return@addOnCompleteListener
                }
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        // Here we get the list of boards in the form of documents.
                        Log.d("Favourite Product", document.toString())

                        val favourite: Favourite = document.toObject(Favourite::class.java)

                        when (activity) {
                            is DestinationDetailsActivity -> activity.successFavouriteDestinationFromFirestore(favourite)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Get Favourites", "Error while checking if destination is in user's favourites.", e)
            }
    }

    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is EditProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is EditProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

}
