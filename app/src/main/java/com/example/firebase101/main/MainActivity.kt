package com.example.firebase101.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.firebase101.R
import com.example.firebase101.controls.RoomControlActivity
import com.example.firebase101.dashboard.DashboardActivity
import com.example.firebase101.userAuth.LoginActivity
import com.example.firebase101.userAuth.UserActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    lateinit var txtS1: TextView
    lateinit var txtS2: TextView
    lateinit var txtS3: TextView
    lateinit var txtS4: TextView
    lateinit var txtS6: TextView
    lateinit var txtDate: TextView

    lateinit var database: DatabaseReference
    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    var db: FirebaseDatabase? = null
    var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        listeners()

        /*    val toolbar = findViewById<View>(R.id.toolbar)
            toolbar.findViewById<View>(R.id.tool_bar_left_icon).setOnClickListener {
                Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
            }
            toolbar.findViewById<View>(R.id.toolbarMenu).setOnClickListener {
                Toast.makeText(this, "Sa", Toast.LENGTH_SHORT).show()
            }
    */
        initAuthStateListener()
        readData()
    }

    private fun init() {
        txtS1 = findViewById(R.id.sensorValue1)
        txtS2 = findViewById(R.id.sensorValue2)
        txtS3 = findViewById(R.id.sensorValue3)
        txtS4 = findViewById(R.id.lampControl)
        txtS6 = findViewById(R.id.sensorValue6)
        txtDate = findViewById(R.id.updated_date)
        //txtMainTemp = findViewById(R.id.mainTemp)
    }

    private fun listeners() {
        val llS1 = findViewById<LinearLayout>(R.id.llSensor1)
        llS1.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("sensorName", "sensor1")
            startActivity(intent)
        }

        val llS2 = findViewById<LinearLayout>(R.id.llSensor2)
        llS2.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("sensorName", "sensor2")
            startActivity(intent)
        }

        val llS3 = findViewById<LinearLayout>(R.id.llSensor3)
        llS3.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("sensorName", "sensor3")
            startActivity(intent)
        }

        txtDate.setOnClickListener {

        }

        val llS4 = findViewById<LinearLayout>(R.id.llLampControl)
        llS4.setOnClickListener {
            startActivity(Intent(this, RoomControlActivity::class.java))
        }

        val llS5 = findViewById<LinearLayout>(R.id.llUser)
        llS5.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }
    }

    private fun initAuthStateListener() {
        authStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val user = p0.currentUser
                if (user != null) {
                    //do it
                } else {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
        val sdf = SimpleDateFormat("dd/MMMM/yyyy")
        val currentDate = sdf.format(Date())
        txtDate.text = currentDate
    }

    // Control the user come by back button
    //if user come by back button system remove the user
    private fun controlUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // user can't back to main menu
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        if (authStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
        }
        super.onStop()
    }


    private fun readData() {
        val references = FirebaseDatabase.getInstance().reference
        val query = references.child("pi")
            .orderByKey()
            .equalTo("sensors")
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
                    val readedData = singleSnapshot.getValue(Sensor::class.java)
                    //txtMainTemp.text = readedData?.Sensor1.toString().plus("°C")

                    txtS1.text = readedData?.Sensor1.toString()
                    txtS2.text = readedData?.Sensor2.toString()
                    txtS3.text = readedData?.Sensor3.toString()

                    val valueList = ArrayList<SensorItem>()
                    valueList.add(
                        SensorItem(
                            "Instant Temprature",
                            readedData?.Sensor1.toString()
                        )
                    )
                    valueList.add(
                        SensorItem(
                            "Instant Humudity",
                            readedData?.Sensor2.toString()
                        )
                    )
                    valueList.add(
                        SensorItem(
                            "Instant Pressure",
                            readedData?.Sensor3.toString()
                        )
                    )

                    initViewPager(valueList)
                    Log.d("Tag", readedData.toString())
                }
            }
        })
    }

    private fun initViewPager(valueList: ArrayList<SensorItem>) {
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        viewPager.adapter = SensorValueAdapter(valueList)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

        }.attach()

        val update = Runnable {
            if (currentPosition == valueList.size) {
                currentPosition = 0
            }
            viewPager.setCurrentItem(currentPosition++, true)
        }

        val handler = Handler()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 0, 3500)
    }
}