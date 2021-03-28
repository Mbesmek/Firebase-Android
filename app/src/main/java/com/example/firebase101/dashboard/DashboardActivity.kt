package com.example.firebase101.dashboard

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.example.firebase101.*
import com.example.firebase101.main.MainActivity
import com.example.firebase101.main.TimeRange
import com.example.firebase101.utils.Utility
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class DashboardActivity : AppCompatActivity() {
    lateinit var entries: ArrayList<Entry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val sensorName = intent.getStringExtra("sensorName")
        listeners()

        if (sensorName != null) {
            readFirestore(1, TimeRange.DAY, sensorName)
        }

    }

    private fun listeners() {
        val btnDay = findViewById<Button>(R.id.btnDay)
        val btnWek = findViewById<Button>(R.id.btnWeek)
        val btnMonth = findViewById<Button>(R.id.btnMonth)
        val sensorName = intent.getStringExtra("sensorName")
        val btnBack = findViewById<ImageView>(R.id.tool_bar_left_icon)

        btnDay.setOnClickListener {
            if (sensorName != null) {
                readFirestore(
                    1,
                    TimeRange.DAY, sensorName
                )
            }
        }
        btnWek.setOnClickListener {
            if (sensorName != null) {
                readFirestore(
                    7,
                    TimeRange.WEEK, sensorName
                )
            }
        }
        btnMonth.setOnClickListener {
            if (sensorName != null) {
                readFirestore(
                    30,
                    TimeRange.MONTH, sensorName
                )
            }
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, MainActivity::class.java))
        }
    }

    private fun setLine(descriptionText: String) {
        val lineChart = findViewById<com.github.mikephil.charting.charts.LineChart>(
            R.id.lineChart
        )
        lineChart.clear()

        val vl = LineDataSet(entries, "Sensor Value")


        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = Utility.getColor(
            this,
            R.color.gray
        )
        vl.fillAlpha = Utility.getColor(
            this,
            R.color.red
        )


        lineChart.fitScreen()
//        lineChart.zoomOut()
        lineChart.xAxis.labelRotationAngle = 0f


        lineChart.data = LineData(vl)


        lineChart.axisRight.isEnabled = false
//        lineChart.xAxis.axisMaximum = 0.1f


        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)


        lineChart.description.text = descriptionText
        lineChart.setNoDataText("No Sensor Value yet!")


        lineChart.animateX(500, Easing.EaseInExpo)


        val markerView = CustomMarker(
            this@DashboardActivity,
            R.layout.marker_view
        )
        lineChart.marker = markerView

    }

    fun readFirestore(minusDay: Long, type: TimeRange, sensorName: String) {
        entries = ArrayList<Entry>()
        val db = FirebaseFirestore.getInstance()
        var descriptionText = "DAYS"
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

                            TimeRange.DAY -> if (str != null) {
                                entries.add(Entry(date.hour.toFloat(), str.toFloat()))
                                descriptionText = "DAY"
                                System.out.println("Day:" + date.hour.toFloat() + ";" + str.toFloat())
                            }
                            TimeRange.WEEK -> if (str != null) {
                                entries.add(Entry(date.dayOfMonth.toFloat(), str.toFloat()))
                                descriptionText = "WEEK"
                                System.out.println("Week:" + date.dayOfMonth.toFloat() + ";" + str.toFloat())
                            }
                            TimeRange.MONTH -> if (str != null) {
                                entries.add(Entry(date.dayOfMonth.toFloat(), str.toFloat()))
                                descriptionText = "MONTHS"
                                System.out.println("Month:" + date.hour.toFloat() + ";" + str.toFloat())
                            }
                        }

                    }

                }
                setLine(descriptionText)
            }
            .addOnFailureListener { exception ->
                Log.w("Oku", "Error getting documents.", exception)
            }
    }
}