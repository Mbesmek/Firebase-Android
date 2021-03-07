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
    fun  readFirestore(minusDay:Long) :List<Entry>  {
        val valueList = ArrayList<Entry>()
        val db= FirebaseFirestore.getInstance()

        val currentDate = LocalDateTime.now().minusDays(minusDay)
       System.out.println(" C DATE is  $currentDate")
//        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        db.collection("sensor")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
//                    val formatter = DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss.SSS")
                    val date = LocalDateTime.parse(document.id, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    System.out.println(" C DATE is  "+date.isAfter(currentDate))
                    valueList.add(Entry(1f,2f))
                        Log.d("Oku", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Oku", "Error getting documents.", exception)
            }
        return valueList
    }
}