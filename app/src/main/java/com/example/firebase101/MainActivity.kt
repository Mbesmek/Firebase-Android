package com.example.firebase101

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {
    private var progr = 0
    private var progress_bar: ProgressBar? = null
    lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var database: DatabaseReference
      var txtS1: TextView? =null
//    var txtS1: TextView = findViewById<TextView>(R.id.txtTempProgress)
    var db: FirebaseDatabase? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAuthStateListener()
        progress_bar = findViewById<ProgressBar>(R.id.temperatureBar) as ProgressBar
        var btnOpenDashboard1 = findViewById<Button>(R.id.btnSensor1Dash)
        readData()

        btnOpenDashboard1.setOnClickListener {

            var intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)

        }

    }

    private fun initAuthStateListener() {
        authStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = p0.currentUser
                if (user != null) {

                } else {
                    var intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }

            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menuLogout -> {
                logOut()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()

    }

    override fun onResume() {
        super.onResume()
        controlUser()
    }

    // Control the user come by back button
    //if user come by back button system remove the user
    private fun controlUser() {
        var user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            var intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // user can't back to main menu
            startActivity(intent)
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        if (authStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
        }
    }


    private fun readData() {

        var txtS1=findViewById<TextView>(R.id.txtTempProgress)
        var references = FirebaseDatabase.getInstance().reference
//        query1
        var query = references.child("pi")
                .orderByKey()
                .equalTo("sensors")
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
                    var readedData = singleSnapshot.getValue(Sensors::class.java)
                    txtS1.text = readedData?.sensorName
//                    txtS2.text = readedData?.sensorValue
                    updateProgress()
                    Log.d("Tag", readedData.toString())

                }
            }
        })
    }

    private fun updateProgress(){
        progress_bar
        progress_bar?.progress = 50
        txtS1?.text = txtS1?.text
    }

    private fun readFirestore2() {
        val db = FirebaseFirestore.getInstance()

        db.collection("sensor")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id.startsWith("2021-03-02"))
                            Log.d("Oku", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Oku", "Error getting documents.", exception)
                }
    }


}