package com.unipi.torpiles.cyprustraveler.ui.activities

import Constants
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.database.FirestoreHelper
import com.unipi.torpiles.cyprustraveler.databinding.ActivitySignInBinding
import com.unipi.torpiles.cyprustraveler.models.User
import com.unipi.torpiles.cyprustraveler.utils.IntentUtils
import com.unipi.torpiles.cyprustraveler.utils.SnackBarErrorClass
import com.unipi.torpiles.cyprustraveler.utils.SnackBarSuccessClass


class SignInActivity : BaseActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
    }

    private fun init() {
        if (intent.hasExtra(Constants.EXTRA_REG_USERS_SNACKBAR)) {
            if (intent.extras?.getBoolean(Constants.EXTRA_REG_USERS_SNACKBAR) == true) {
                SnackBarSuccessClass
                    .make(binding.root, getString(R.string.txt_congratulations_2))
                    .show()
            }
            binding.inputTxtEmail.setText(intent.getStringExtra(Constants.EXTRA_USER_EMAIL))
        }

        setupUI()
        setupActionBar()
        setupClickListeners()
    }

    private fun setupUI() {
        binding.apply {
            inputTxtEmail.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    inputTxtLayoutEmail.isErrorEnabled = false
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            inputTxtPassword.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    inputTxtLayoutPassword.isErrorEnabled = false
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            txtViewSignUp.setOnClickListener {
                // Launch the sign up screen when the user clicks on the sign up text.
                val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
            txtViewForgotPassword.setOnClickListener {
                // Launch the forgot password screen when the user clicks on the forgot password text.
                val intent = Intent(this@SignInActivity, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
            btnSignIn.setOnClickListener { signInUser() }
        }
    }

    private fun signInUser() {
        if (validateFields()) {
            // Show the progress dialog.
            showProgressDialog()

            binding.apply {
                // Get the text from editText and trim the space
                val email = inputTxtEmail.text.toString().trim { it <= ' ' }
                val password = inputTxtPassword.text.toString().trim { it <= ' ' }

                // Log-In using FirebaseAuth
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            FirestoreHelper().getUserDetails(this@SignInActivity)
                        } else {
                            // Hide the progress dialog
                            hideProgressDialog()
                            SnackBarErrorClass
                                .make(root, task.exception!!.message.toString())
                                .show()
                        }
                    }
            }
        }
        else
            binding.btnSignIn.startAnimation(AnimationUtils.loadAnimation(this@SignInActivity, R.anim.shake))
    }

    /**
     * A function to notify user that logged in success and get the user details from the FireStore database after authentication.
     */
    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        IntentUtils().goToMainActivity(this@SignInActivity)
        finish()
    }

    private fun validateFields(): Boolean {
        binding.apply {
            return when {
                TextUtils.isEmpty(inputTxtEmail.text.toString().trim { it <= ' ' }) -> {
                    SnackBarErrorClass
                        .make(root, getString(R.string.txt_error_empty_email))
                        .show()
                    inputTxtLayoutEmail.requestFocus()
                    inputTxtLayoutEmail.error = getString(R.string.txt_error_empty_email)
                    false
                }

                TextUtils.isEmpty(inputTxtPassword.text.toString().trim { it <= ' ' }) -> {
                    SnackBarErrorClass
                        .make(root, getString(R.string.txt_error_empty_password))
                        .show()
                    inputTxtLayoutPassword.requestFocus()
                    inputTxtLayoutPassword.error = getString(R.string.txt_error_empty_password)
                    false
                }

                else -> true
            }
        }
    }

    private fun setupActionBar() {
        binding.toolbar.apply {
            setSupportActionBar(root)
            textViewActionBarLabel.text = getString(R.string.txt_sign_in)

        }

        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorContainer)))
            it.setHomeAsUpIndicator(R.drawable.ic_chevron_left_24dp)
        }
    }
}
