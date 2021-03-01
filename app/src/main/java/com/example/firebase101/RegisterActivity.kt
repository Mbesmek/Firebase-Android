package com.example.firebase101

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnSingup = findViewById<Button>(R.id.btnSignUp)
        val edtMail = findViewById<EditText>(R.id.edtEmail)
        val edtPswd= findViewById<EditText>(R.id.edtPassword)
        val edtRePswd= findViewById<EditText>(R.id.edtRePassword)
            btnSingup.setOnClickListener {

                if (edtMail.text.isNotEmpty()&& edtPswd.text.isNotEmpty()&& edtRePswd.text.isNotEmpty()){
                    if (edtPswd.text.toString().equals(edtRePswd.text.toString())){
                        
                        registerNewUser(edtMail.text.toString(),edtPswd.text.toString())
                        
                    }else
                    {
                        Toast.makeText(this,"Passwords are not match",Toast.LENGTH_SHORT).show()
                    }
                    
                }else
                {
                    Toast.makeText(this,"Fill whole field",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerNewUser(mail: String, password: String) {
        progressBarVisible(true)
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(mail,password)
                .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                    override fun onComplete(p0: Task<AuthResult>) {
                        if (p0.isSuccessful){
                       Toast.makeText(this@RegisterActivity,"Sign Up with Succesfuly",Toast.LENGTH_SHORT).show()
                            sendVerifiedMail()

                            var user=User()
                            user.name

                            FirebaseAuth.getInstance().signOut()
                            var intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                    }else{
                            Toast.makeText(this@RegisterActivity,"Error occured, While signing up user "+p0.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        progressBarVisible(false)
    }


    private fun sendVerifiedMail (){
        var user=FirebaseAuth.getInstance().currentUser
        if (user!=null){
            user.sendEmailVerification()

                    .addOnCompleteListener { p0 ->
                        if (p0.isSuccessful){

                            Toast.makeText(this@RegisterActivity,"Please Verified Your Account with Your Mail : ",Toast.LENGTH_SHORT).show()
                        } else{
                            Toast.makeText(this@RegisterActivity,"Error occured, While sending verified mail  "+p0.exception?.message,Toast.LENGTH_SHORT).show()

                        }
                    }
        }
    }

    private fun progressBarVisible(state : Boolean){
        val progressBar=findViewById<ProgressBar>(R.id.progressBar)
        if (state)
            progressBar.visibility=View.VISIBLE
        else
            progressBar.visibility=View.INVISIBLE
    }
}