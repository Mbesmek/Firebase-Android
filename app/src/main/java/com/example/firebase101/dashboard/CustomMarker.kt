package com.example.firebase101.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.example.firebase101.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@SuppressLint("ViewConstructor")
class CustomMarker constructor(context: Context, layoutResource: Int) :
    MarkerView(context, layoutResource) {
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val sensorV = findViewById<TextView>(R.id.sensorValue)
        val value = entry?.y?.toDouble() ?: 0.0
        var resText = ""
        resText = if (value.toString().length > 8) {
            "Val: " + value.toString().substring(0, 7)
        } else {
            "Val: $value"
        }
        sensorV.text = resText
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}