package com.example.firebase101

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class DashboardActivity : AppCompatActivity() {
    lateinit var entries: ArrayList<Entry>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        listeners()

        readFirestore(1, TimeRange.DAY, "sensor1")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listeners() {
        val btnDay=findViewById<Button>(R.id.btnDay)
        val btnWekk=findViewById<Button>(R.id.btnWeek)
        val btnMonth=findViewById<Button>(R.id.btnMonth)

        btnDay.setOnClickListener {
            readFirestore(1, TimeRange.DAY, "sensor1")
        }
        btnWekk.setOnClickListener {
            readFirestore(1, TimeRange.WEEK, "sensor1")
        }
        btnWekk.setOnClickListener {
            readFirestore(1, TimeRange.MONTH, "sensor1")
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLine() {
        val lineChart = findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)
        lineChart.clear()

        val vl = LineDataSet(entries, "My Type")

//Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = Utility.getColor(this, R.color.gray)
        vl.fillAlpha = Utility.getColor(this, R.color.red)

//Part5
        lineChart.fitScreen()
//        lineChart.zoomOut()
        lineChart.xAxis.labelRotationAngle = 0f

//Part6
        lineChart.data = LineData(vl)

//Part7
        lineChart.axisRight.isEnabled = false
//        lineChart.xAxis.axisMaximum = 0.1f

//Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

//Part9
        lineChart.description.text = "Days"
        lineChart.setNoDataText("No forex yet!")

//Part10
        lineChart.animateX(500, Easing.EaseInExpo)

//Part11
        val markerView = CustomMarker(this@DashboardActivity, R.layout.marker_view)
        lineChart.marker = markerView

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun readFirestore(minusDay: Long, type: TimeRange, sensorName: String) {
        entries = ArrayList<Entry>()
        val db = FirebaseFirestore.getInstance()

        val currentDate = LocalDateTime.now().minusDays(minusDay)



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

                                TimeRange.WEEK-> if (str != null) {
                                    entries.add(Entry(date.dayOfMonth.toFloat(), str.toFloat()))
                                    System.out.println("Week:" + date.dayOfMonth.toFloat() + ";" + str.toFloat())
                                }
                                TimeRange.DAY -> if (str != null) {
                                    entries.add(Entry(date.hour.toFloat(), str.toFloat()))
                                    System.out.println("Day:" + date.hour.toFloat() + ";" + str.toFloat())
                                }
                                TimeRange.MONTH ->if (str != null) {
                                    entries.add(Entry(date.monthValue.toFloat(), str.toFloat()))
                                    System.out.println("Month:" + date.hour.toFloat() + ";" + str.toFloat())
                                }
                            }

                        }

                    }
                    setLine()

                }

                .addOnFailureListener { exception ->
                    Log.w("Oku", "Error getting documents.", exception)
                }


    }
}