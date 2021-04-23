package com.example.firebase101.main

import java.lang.reflect.Constructor

enum class TimeRange {
    DAY, WEEK, MONTH
}

data class Sensor(

    var Sensor1: String? ,
    var Sensor2: String? ,
    var Sensor3: String?

)

data class User(
    var name: String? = null,
    var phone: String? = null,
    var profileImage: String? = null,
    var userId: String? = null
)

data class SensorItem(
    val header: String,
    val value: String
)