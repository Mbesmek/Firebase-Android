package com.example.firebase101

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class DashboardActivity : AppCompatActivity() {
    lateinit var entries:ArrayList<Entry>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        readFirestore(7,"day","sensor1")

    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLine(){
        val lineChart=findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)



        val vl = LineDataSet(entries, "My Type")


        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.gray
        vl.fillAlpha = R.color.red


        lineChart.xAxis.labelRotationAngle = 0f


        lineChart.data = LineData(vl)

        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.axisMaximum = 0.1f


        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)


        lineChart.description.text = "Days"
        lineChart.setNoDataText("No forex yet!")


        lineChart.animateX(1800, Easing.EaseInExpo)



    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun readFirestore(minusDay: Long, type: String, sensorName: String){
        entries=ArrayList<Entry>()
        val db = FirebaseFirestore.getInstance()

        val currentDate = LocalDateTime.now().minusDays(minusDay)
        System.out.println(" C DATE is  $currentDate")


        db.collection("sensor")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {

                        val date = LocalDateTime.parse(
                                document.id,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        )
                        val day = date.dayOfMonth.toFloat()
                        val str = document.getDouble(sensorName)?.toFloat()
                        if (date.isAfter(currentDate)) {
                            when (type) {

                                "day" -> if (str != null) {
                                    entries.add(Entry(date.dayOfMonth.toFloat(), str.toFloat()))
                                     System.out.println("Data:" +date.dayOfMonth.toFloat()+";"+str.toFloat())
                                }
                            }
//                        System.out.println("Data:" + document.getString("sensor1"))
                            Log.d("Oku", "${document.id} => ${document.data}")
                        }

                    }
                    setLine()

                }

                .addOnFailureListener { exception ->
                    Log.w("Oku", "Error getting documents.", exception)
                }


    }
}