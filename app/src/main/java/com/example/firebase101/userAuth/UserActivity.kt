package com.example.firebase101.userAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.firebase101.R
import com.example.firebase101.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        listener()
    }

    private fun listener() {


        val imgBackButton = findViewById<ImageView>(R.id.tool_bar_left_icon)

        imgBackButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}