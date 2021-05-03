package com.example.firebase101.Security

import android.os.Build
import androidx.annotation.RequiresApi
import io.grpc.KnownLength

@RequiresApi(Build.VERSION_CODES.O)
fun encryptData(data:String):String {
    val key = generateString(32)
    val encryptedData= AESEnc.encrypt(data,key)

    return encryptedData
}

fun generateString(length:Int):String{
     val STRING_CHARACTERS = ('0'..'z').toList().toTypedArray()
    val key = (1..length).map { STRING_CHARACTERS.random() }.joinToString("")
    return key
}
