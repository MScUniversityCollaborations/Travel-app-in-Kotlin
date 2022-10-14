package com.unipi.torpiles.cyprustraveler.ui.activities

import Constants
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.database.FirestoreHelper
import com.unipi.torpiles.cyprustraveler.databinding.ActivityEditProfileBinding
import com.unipi.torpiles.cyprustraveler.models.User
import com.unipi.torpiles.cyprustraveler.utils.GlideLoader
import com.unipi.torpiles.cyprustraveler.utils.SnackBarErrorClass
import java.io.IOException


class EditProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    // Instance of User data model class. We will initialize it later on.
    private lateinit var mUserDetails: User

    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    private var mUserProfileImageURL: String = ""

    private val activityResultLauncherGetImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                try {
                    // The uri of selected image from phone storage.
                    mSelectedImageFileUri = it.data!!.data!!

                        GlideLoader(this@EditProfileActivity).loadUserPicture(
                            mSelectedImageFileUri!!,
                            binding.imgUserPictureFrame
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@EditProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }
        else if (it.resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    // val startActivityForResult: ActivityResultLauncher<Intent> = registerForActivityResult()

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setupUI()

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!

            binding.apply {
                inputTxtFullName.setText(mUserDetails.fullName)
                countryCodePicker.setCountryForPhoneCode(mUserDetails.phoneCode)
                inputTxtPhoneNumber.setText(mUserDetails.phoneNumber)
                // Set gender in radio button
                if (mUserDetails.gender != -1)
                    if (mUserDetails.gender == 0)
                        rbGenderMale.isChecked = true
                    else
                        rbGenderFemale.isChecked = true
                // Set Interests in check boxes
                cbAdventure.isChecked = mUserDetails.adventure == 0
                cbSport.isChecked = mUserDetails.sport == 0
                cbRelaxation.isChecked = mUserDetails.relaxation == 0
                cbAttractions.isChecked = mUserDetails.attractions == 0
            }
        }
    }

    private fun uploadPicture() {

        if (validateFields()) {
            showProgressDialog()

            if (mSelectedImageFileUri != null) {

                FirestoreHelper().uploadImageToCloudStorage(
                    this@EditProfileActivity,
                    mSelectedImageFileUri,
                    Constants.FIELD_IMG_URL
                )
            } else {
                updateProfileToFirestore()
            }
        }
    }

    private fun updateProfileToFirestore() {

        val userHashMap = HashMap<String, Any>()

        // Here we get the text from editText and trim the space
        val inputFullName = binding.inputTxtFullName.text.toString().trim { it <= ' ' }
        val inputPhoneCode = binding.countryCodePicker.selectedCountryCodeAsInt
        val inputPhoneNumber = binding.inputTxtPhoneNumber.text.toString().trim { it <= ' ' }

        binding.apply {
            if (inputFullName != mUserDetails.fullName) {
                userHashMap[Constants.FIELD_FULL_NAME] = inputFullName
            }

            // 0: Selected
            // 1: Not Selected
            val adventure = if (cbAdventure.isChecked) 0 else 1
            val sport = if (cbSport.isChecked) 0 else 1
            val relaxation = if (cbRelaxation.isChecked) 0 else 1
            val attractions = if (cbAttractions.isChecked) 0 else 1


            // -1: Not set
            // 0: Male
            // 1: Female
            var gender = -1
            if (rbGenderMale.isChecked)
                gender = 0
            else if (rbGenderFemale.isChecked)
                gender = 1

            if (mUserProfileImageURL.isNotEmpty()) {
                userHashMap[Constants.FIELD_IMG_URL] = mUserProfileImageURL
            }

            if (inputPhoneNumber.isNotEmpty() && inputPhoneNumber != mUserDetails.phoneNumber) {
                userHashMap[Constants.FIELD_PHONE_NUMBER] = inputPhoneNumber
            }

            if (gender != mUserDetails.gender) {
                userHashMap[Constants.FIELD_GENDER] = gender
            }

            if(adventure != mUserDetails.adventure) userHashMap[Constants.FIELD_ADVENTURE] = adventure

            if(sport != mUserDetails.sport) userHashMap[Constants.FIELD_SPORT] = sport

            if(relaxation != mUserDetails.relaxation) userHashMap[Constants.FIELD_RELAXATION] = relaxation

            if(attractions != mUserDetails.attractions) userHashMap[Constants.FIELD_ATTRACTIONS] = attractions

            if (inputPhoneCode != 0) {
                userHashMap[Constants.FIELD_PHONE_CODE] = inputPhoneCode
            }

            // Here if user is about to complete the profile then update the field or else no need.
            // false: User profile is incomplete.
            // true: User profile is completed.
            if (!mUserDetails.profileCompleted && (gender != -1 && inputPhoneNumber != "" && inputPhoneCode != 0 && mUserProfileImageURL != "")) {
                userHashMap[Constants.FIELD_COMPLETE_PROFILE] = true
            }

            FirestoreHelper().updateProfile(this@EditProfileActivity, userHashMap)
        }
    }

    fun successUpdateProfileToFirestore() {

        // Hide progress dialog
        hideProgressDialog()

        Toast.makeText(
            this,
            resources.getString(R.string.txt_profile_updated),
            Toast.LENGTH_SHORT
        ).show()

        setResult(RESULT_OK)
        finish()
    }

    private fun validateFields(): Boolean {
        binding.apply {
            return when {
                TextUtils.isEmpty(inputTxtFullName.text.toString().trim { it <= ' ' }) -> {
                    SnackBarErrorClass
                        .make(root, getString(R.string.txt_error_empty_name))
                        .setBehavior(Constants.SNACKBAR_BEHAVIOR)
                        .show()
                    inputTxtLayoutFullName.requestFocus()
                    inputTxtLayoutFullName.error = getString(R.string.txt_error_empty_name)
                    false
                }

                TextUtils.isEmpty(inputTxtPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                    SnackBarErrorClass
                        .make(root, getString(R.string.txt_error_empty_phone))
                        .setBehavior(Constants.SNACKBAR_BEHAVIOR)
                        .show()
                    inputTxtLayoutPhoneNumber.requestFocus()
                    inputTxtLayoutPhoneNumber.error = getString(R.string.txt_error_empty_phone)
                    false
                }

                else -> true
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            imgUserPictureFrame.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this@EditProfileActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    Constants.showImageChooser(activityResultLauncherGetImage)
                    //Constants.showImageChooser(this@EditProfileActivity)
                }
                else {
                    /*Requests permissions to be granted to this application. These permissions
                     must be requested in your manifest, they should not be granted to your app,
                     and they should have protection level*/
                    ActivityCompat.requestPermissions(
                        this@EditProfileActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE
                    )
                }
            }
        }
    }

    private fun setupUI() {
        setupClickListeners()
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar.root)

        val actionBar = supportActionBar
        binding.apply {
            toolbar.imgBtnSave.setOnClickListener {
                uploadPicture()
            }
        }
        actionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorContainer)))
            it.setHomeAsUpIndicator(R.drawable.ic_chevron_left_24dp)
        }
    }

    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(activityResultLauncherGetImage)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * A function to notify the success result of image upload to the Cloud Storage.
     *
     * @param imageURL After successful upload the Firebase Cloud returns the URL.
     */
    fun imageUploadSuccess(imageURL: String) {

        mUserProfileImageURL = imageURL

        updateProfileToFirestore()
    }
}
