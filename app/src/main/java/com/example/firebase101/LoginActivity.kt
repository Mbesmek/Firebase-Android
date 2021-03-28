package com.example.firebase101

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initAuthStateListener()

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
/*
        txtResendMail.setOnClickListener {
            val showDialog = ResendMailFragment()
            showDialog.show(supportFragmentManager, "showDialog")
        }
*/

        txtForgetPassword.setOnClickListener {
            val showDialog = ForgetPasswordDialogFragment()
            showDialog.show(supportFragmentManager, "showDialog")
        }

        btnLogin.setOnClickListener {

            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                progressBarVisible(true)
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                        override fun onComplete(p0: Task<AuthResult>) {

                            if (p0.isSuccessful) {
                                progressBarVisible(false)
//                                        if(!p0.result?.user!!.isEmailVerified)
//                                            FirebaseAuth.getInstance().signOut()
//                                    Toast.makeText(this@LoginActivity, "Login Succesful " + FirebaseAuth.getInstance().currentUser?.email, Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Error occurred, While user logging  " + p0.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBarVisible(false)
                            }
                        }

                    })
//                        .addOnCompleteListener { p0->{ p0.isSuccessful} } lambda
            } else {
                Toast.makeText(this@LoginActivity, "Please fill empty fields", Toast.LENGTH_SHORT)
                    .show()

            }
            progressBarVisible(false)
        }


    }

    private fun progressBarVisible(state: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBarLogin)
        if (state)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.INVISIBLE
    }

    // Control user login and sign out
    private fun initAuthStateListener() {
        authStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = p0.currentUser
                if (user != null) {
                    if (user.isEmailVerified) {
//                        Toast.makeText(this@LoginActivity, " Mail Address is Verified", Toast.LENGTH_SHORT).show()

                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()// if user login, can't back
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Please Verified Your Mail Address",
                            Toast.LENGTH_SHORT
                        ).show()
//                        FirebaseAuth.getInstance().signOut()
                    }
                }
            }

        }

    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }
}