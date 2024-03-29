package com.unipi.torpiles.cyprustraveler.ui.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.unipi.torpiles.cyprustraveler.R
import com.unipi.torpiles.cyprustraveler.databinding.ActivityForgotPasswordBinding
import com.unipi.torpiles.cyprustraveler.utils.SnackBarErrorClass

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
    }

    private fun init() {
        setupUI()
        setUpActionBar()
        setUpClickListeners()
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
        }
    }

    private fun setUpClickListeners() {
        binding.apply {
            btnSend.setOnClickListener{ resetPassword() }
        }
    }


    private fun resetPassword() {
        binding.apply {
            if (validateFields()) {
                // Show the progress dialog.
                showProgressDialog()

                // This piece of code is used to send the reset password link to the user's email id if the user is registered.
                FirebaseAuth.getInstance().sendPasswordResetEmail(inputTxtEmail.text.toString())
                    .addOnCompleteListener { task ->

                        // Hide the progress dialog
                        hideProgressDialog()

                        if (task.isSuccessful) {
                            /*SnackBarSuccessClass
                                .make(root, getString(R.string.txt_password_reset_mail_sent))
                                .show()*/

                            finish()
                        } else {
                            SnackBarErrorClass
                                .make(root, task.exception!!.message.toString())
                                .show()
                        }
                    }
            }
            else
                btnSend.startAnimation(AnimationUtils.loadAnimation(this@ForgotPasswordActivity, R.anim.shake))
        }
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

                else -> true
            }
        }
    }

    private fun setUpActionBar() {
        binding.toolbar.apply {
            setSupportActionBar(root)
            textViewActionBarLabel.text = getString(R.string.txt_forgot_password)
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
