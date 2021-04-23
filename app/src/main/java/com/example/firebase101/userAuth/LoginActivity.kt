package com.example.firebase101.userAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.firebase101.main.MainActivity
import com.example.firebase101.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initAuthStateListener()
        initListeners()
    }

    private fun progressBarVisible(state: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBarLogin)
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun initListeners() {
        //val txtResendMail = findViewById<TextView>(R.id.txtResendMail)
        val txtForgetPassword = findViewById<TextView>(R.id.txtForgetPswd)
        val txtSignUp = findViewById<TextView>(R.id.txtLoginSignUp)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val email = findViewById<TextView>(R.id.edtLoginMail)
        val password = findViewById<TextView>(R.id.edtLoginPswd)

        txtSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        txtForgetPassword.setOnClickListener {
            val showDialog =
                ForgetPasswordDialogFragment()
            showDialog.show(supportFragmentManager, "showDialog")
        }

        btnLogin.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                progressBarVisible(true)
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener { p0 ->
                        if (p0.isSuccessful) {
                            progressBarVisible(false)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Error occurred, While user logging  " + p0.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBarVisible(false)
                        }
                    }
            } else {
                Toast.makeText(this@LoginActivity, "Please fill empty fields", Toast.LENGTH_SHORT)
                    .show()
            }
            progressBarVisible(false)
        }
    }

    // Control user login and sign out
    private fun initAuthStateListener() {
        authStateListener = FirebaseAuth.AuthStateListener { p0 ->
            val user = p0.currentUser
            if (user != null) {
                if (user.isEmailVerified) {
                    //Toast.makeText(this@LoginActivity, " Mail Address is Verified", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()// if user login, can't back
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please Verified Your Mail Address",
                        Toast.LENGTH_SHORT
                    ).show()
                    //FirebaseAuth.getInstance().signOut()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
        super.onStop()
    }
}