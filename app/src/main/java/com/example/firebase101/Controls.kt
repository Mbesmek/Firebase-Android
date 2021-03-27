package com.example.firebase101

class Controls {
    lateinit var electric1: String
    lateinit var electric2: String
    lateinit var electric3: String

    lateinit var lamp1: String
    lateinit var lamp2: String
    lateinit var lamp3: String

    constructor()


    constructor(electric1: String, electric2: String, electric3: String, lamp1: String, lamp2: String, lamp3: String) {
        this.electric1 = electric1
        this.electric2 = electric2
        this.electric3 = electric3
        this.lamp1 = lamp1
        this.lamp2 = lamp2
        this.lamp3 = lamp3
    }
}