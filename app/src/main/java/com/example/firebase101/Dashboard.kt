package com.example.firebase101

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Dashboard() {


    @RequiresApi(Build.VERSION_CODES.O)
    fun readFirestore(minusDay: Long, type: String, sensorName: String): ArrayList<Entry> {
        val valueList = ArrayList<Entry>()
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
                                valueList.add(Entry(date.dayOfMonth.toFloat(), str.toFloat()))
                            }
                        }
//                        System.out.println("Data:" + document.getString("sensor1"))
                        Log.d("Oku", "${document.id} => ${document.data}")
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w("Oku", "Error getting documents.", exception)
            }
        return valueList
    }
}