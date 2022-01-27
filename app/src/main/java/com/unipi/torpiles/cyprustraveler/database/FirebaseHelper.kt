package com.unipi.torpiles.cyprustraveler.database

import Constants
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.unipi.torpiles.cyprustraveler.models.Destination
import com.unipi.torpiles.cyprustraveler.ui.fragments.HomeFragment

class FirestoreHelper {

    // Access a Cloud Firestore instance.
    private val dbFirestore = FirebaseFirestore.getInstance()


    // region -User Management-
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
     * A function to get the destinations list from cloud firestore.
     *
     * @param fragment The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    fun getDestinationsList(fragment: Fragment) {
        dbFirestore.collection(Constants.COLLECTION_DESTINATIONS)
            .limit(10)
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

}
