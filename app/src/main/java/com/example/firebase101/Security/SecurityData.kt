package com.example.firebase101.Security

import android.os.Build
import androidx.annotation.RequiresApi
@RequiresApi(Build.VERSION_CODES.O)
object SecurityData{
fun encryptData(data: String): String {
    val key = generateString(32)
    val encryptedData = AESEnc.encrypt(data, key)
    val encryptedKey = RSAEnc.encryptRsa(key)
    return encryptedData + encryptedKey
}


fun decryptData(data: String): String {
    val encData = data.substring(0, 24)
    val encKey = data.substring(24, data.length)
    println(encData.length)
    println(encKey.length)
    val key = RSAEnc.decryptRsa(encKey)
    return AESEnc.decrypt(encData, key)

}

fun generateString(length: Int): String {
    val STRING_CHARACTERS = ('0'..'z').toList().toTypedArray()
    val key = (1..length).map { STRING_CHARACTERS.random() }.joinToString("")
    return key
}}