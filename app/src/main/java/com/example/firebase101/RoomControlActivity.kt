package com.example.firebase101

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import kotlin.properties.Delegates

class RoomControlActivity : AppCompatActivity() {

    lateinit var llLamp1: LinearLayout
    lateinit var llLamp2: LinearLayout
    lateinit var llLamp3: LinearLayout
    lateinit var llElectric1: LinearLayout
    lateinit var llElectric2: LinearLayout
    lateinit var llElectric3: LinearLayout
    lateinit var txtLamp1: TextView
    lateinit var txtLamp2: TextView
    lateinit var txtLamp3: TextView
    lateinit var txtElectric1: TextView
    lateinit var txtElectric2: TextView
    lateinit var txtElectric3: TextView

    lateinit var layouts: ArrayList<LinearLayout>
   lateinit var controlValue:ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_control)
        init()
        readData()
        listeners()

    }

    override fun onResume() {
        super.onResume()
        readData()
        listeners()
    }

    private fun init() {
        layouts = ArrayList<LinearLayout>()
        llLamp1 = findViewById<LinearLayout>(R.id.llLampControl1)

        layouts.add(llLamp1)
        llLamp2 = findViewById<LinearLayout>(R.id.llLampControl2)
        layouts.add(llLamp2)
        llLamp3 = findViewById<LinearLayout>(R.id.llLampControl3)
        layouts.add(llLamp3)

        llElectric1 = findViewById<LinearLayout>(R.id.llElectricControl1)
        layouts.add(llElectric1)
        llElectric2 = findViewById<LinearLayout>(R.id.llElectricControl2)
        layouts.add(llElectric2)
        llElectric3 = findViewById<LinearLayout>(R.id.llElectricControl3)
        layouts.add(llElectric3)

        txtLamp1 = findViewById(R.id.txtLampControl1)
        txtLamp2 = findViewById(R.id.txtLampControl2)
        txtLamp3 = findViewById(R.id.txtLampControl3)

        txtElectric1 = findViewById(R.id.txtElectronicControl1)
        txtElectric2 = findViewById(R.id.txtElectronicControl2)
        txtElectric3 = findViewById(R.id.txtElectronicControl3)

    }

    private fun readData() {

        val references = FirebaseDatabase.getInstance().reference
        val query = references.child("pi")
                .orderByKey()
                .equalTo("controls")
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                controlValue = ArrayList<String>()

                for (singleSnapshot in snapshot.children) {
                    val readedData = singleSnapshot.getValue(Controls::class.java)

                    txtLamp1.text = readedData?.lamp1.toString()

                    txtLamp2.text = readedData?.lamp2.toString()

                    txtLamp3.text = readedData?.lamp3.toString()

                    txtElectric1.text = readedData?.electric1.toString()

                    txtElectric2.text = readedData?.electric2.toString()

                    txtElectric3.text = readedData?.electric3.toString()

                    controlValue.add(readedData?.lamp1.toString())
                    controlValue.add(readedData?.lamp2.toString())
                    controlValue.add(readedData?.lamp3.toString())
                    controlValue.add(readedData?.electric1.toString())
                    controlValue.add(readedData?.electric2.toString())
                    controlValue.add(readedData?.electric3.toString())

                    controlLayoutBackground(controlValue)
                    val database=FirebaseDatabase.getInstance().reference
                    database.child("pi").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))

                }
            }
        })


    }


    @SuppressLint("SetTextI18n")
    private fun listeners() {
        val database=FirebaseDatabase.getInstance().reference


        llLamp1.setOnClickListener {
          if(controlValue[0] == "On"){
              controlValue[0]="Off"
              database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
              txtLamp1.text="Off"
              llLamp1.background = ContextCompat.getDrawable(this, R.drawable.curved_background)
          }else{
              controlValue[0]="On"
              database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
              txtLamp1.text="On"
              llLamp1.background = ContextCompat.getDrawable(this, R.drawable.curved_background_clicked)
          }
        }  
        llLamp2.setOnClickListener {
            if(controlValue[1] == "On"){
                controlValue[1]="Off"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtLamp2.text="Off"
                llLamp2.background = ContextCompat.getDrawable(this, R.drawable.curved_background)
            }else{
                controlValue[1]="On"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtLamp2.text="On"
                llLamp2.background = ContextCompat.getDrawable(this, R.drawable.curved_background_clicked)
            }
        }
        llLamp3.setOnClickListener {
            if(controlValue[2] == "On"){
                controlValue[2]="Off"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtLamp3.text="Off"
                llLamp3.background = ContextCompat.getDrawable(this, R.drawable.curved_background)
            }else{
                controlValue[2]="On"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtLamp3.text="On"
                llLamp3.background = ContextCompat.getDrawable(this, R.drawable.curved_background_clicked)
            }
        }

        llElectric1.setOnClickListener {
            if(controlValue[3] == "On"){
                controlValue[3]="Off"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtElectric1.text="Off"
                llElectric1.background = ContextCompat.getDrawable(this, R.drawable.curved_background)
            }else{
                controlValue[3]="On"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtElectric1.text="On"
                llElectric1.background = ContextCompat.getDrawable(this, R.drawable.curved_background_clicked)
            }
        }
        llElectric2.setOnClickListener {
            if(controlValue[4] == "On"){
                controlValue[4]="Off"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtElectric2.text="Off"
                llElectric2.background = ContextCompat.getDrawable(this, R.drawable.curved_background)
            }else{
                controlValue[4]="On"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtElectric2.text="On"
                llElectric2.background = ContextCompat.getDrawable(this, R.drawable.curved_background_clicked)
            }
        }
        llElectric3.setOnClickListener {
            if(controlValue[5] == "On"){
                controlValue[5]="Off"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtElectric3.text="Off"
                llElectric3.background = ContextCompat.getDrawable(this, R.drawable.curved_background)
            }else{
                controlValue[5]="On"
                database.child("mobile").child("controls").setValue(Controls(controlValue[3],controlValue[4],controlValue[5],controlValue[0],controlValue[1],controlValue[2]))
                txtElectric3.text="On"
                llElectric3.background = ContextCompat.getDrawable(this, R.drawable.curved_background_clicked)
            }
        }

    }

    private fun controlLayoutBackground(array: ArrayList<String>) {

        for (i in 0 until array.size) {
            if (array[i] == "On") {
                layouts[i].background = ContextCompat.getDrawable(this, R.drawable.curved_background_clicked)
            }
        }
    }
}