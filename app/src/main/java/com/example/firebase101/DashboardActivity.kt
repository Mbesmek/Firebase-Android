package com.example.firebase101

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class DashboardActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setLine()
    }

    @RequiresApi(Build.VERSION_CODES.O)

    private fun setLine(){
        val lineChart=findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)
        var dashboard=Dashboard()
        var entries:ArrayList<Entry>

        entries=dashboard.readFirestore(7,"day","sensor1")
//        val entries = dashboard.readFirestore(7,"day","sensor1")


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
}