package com.example.firebase101.main

enum class TimeRange {
    DAY, WEEK, MONTH
}

data class Sensor(
    var Sensor1: Long? = null,
    var Sensor2: Long? = null,
    var Sensor3: Long? = null
)

/*
data class Controls(
    var electric1: String,
    var electric2: String,
    var electric3: String,

    var lamp1: String,
    var lamp2: String,
    var lamp3: String
)*/

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