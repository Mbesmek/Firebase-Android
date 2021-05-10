package com.example.firebase101.Security

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.Key
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

@RequiresApi(Build.VERSION_CODES.O)
object RSAEnc {


    fun encryptRsa(data: String): String {
        val publicKey = generateRsaPublicKey()
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val bytes = cipher.doFinal(data.toByteArray())

        return Base64.getEncoder().encodeToString(bytes)
    }

    fun decryptRsa(data: String): String {
        val privateKey = generateRsaPrivateKey()
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val encryptedData = Base64.getDecoder().decode(data);//data.toByteArray()
        val decodedData = cipher.doFinal(encryptedData)
        return String(decodedData)
    }

    fun generateRsaPublicKey(): Key {
        val Public =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEtKt4VW2l2v5MpGcpkplPrvq0CUrflMrhrXQVbIR63eYM+lZsQE/zZHdVhp2BlPhsHuKsHynowi+mtGjmjwGSoLEv7XcyMUtHZeLmCSsN9WfS0QQwe1PmuoMzbqhP/gGVPUJTZjnaY/IcsfzPijJMmlROH/2FJsPxle366tNZ9wIDAQAB"

        val public: ByteArray = Base64.getDecoder().decode(Public)
        val keySpec = X509EncodedKeySpec(public)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }

    fun generateRsaPrivateKey(): Key {
        val privatekey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIS0q3hVbaXa/kykZymSmU+u+rQJSt+UyuGtdBVshHrd5gz6VmxAT/Nkd1WGnYGU+Gwe4qwfKejCL6a0aOaPAZKgsS/tdzIxS0dl4uYJKw31Z9LRBDB7U+a6gzNuqE/+AZU9QlNmOdpj8hyx/M+KMkyaVE4f/YUmw/GV7frq01n3AgMBAAECgYAtXSFcgj94OdNCWdTWFkIIRjV3Q9HvegqceNG83RqwcZhJiW3vlVB/UDGm79x5xnXhZ/FuizylT3RAlV1iqzIK6/Vx7SLoSJG1dK+B9/L8MeWNQ9XoxVdCMRWx8SqlXpqKNqUrs16Op8EPfO6cN5en0DiNXBE7qnwyA/9Prgc62QJBAP6P7yafLYqQ/9l0y7IBiQgtek1Msc8JdaUqpE2o0r2Vy6fAeBTMqKVwkzXwanWmXWnpVarmOuNafTIyL8nAhnUCQQCFdIvJ4qP5GvO0L2gbkhkOh3n37YPS4St0WgV95KQZyYl2+zJOU61tSS0V6ZMvoVI0suWqDG0P1+E9ysQ/CUk7AkEAs3XRSU8Kkhdq52jZeWJvsp4tDHW3HJg0of4P2tfbyd/itR6RCUjG0+srPOmCg/KHBHWE+XhZp5JRlKOg4QCjSQJAR7lJX6k04y5/B7nqw9aTLOHWxU6baHyntBKlnPGC2HEeNhnvAXWrYW7QklRETHbxDW2QTQH8o2Usot8U9aPx/QJAV6AskTWzCBUIG9uJayQRJ5S21CnD/M5X6l/EFXaRf0muxkl8dQvE5E+1S5Xkpm1SUGkc0fpR2fztJCy20x7eJg=="
        val private: ByteArray = Base64.getDecoder().decode(privatekey)
        val keySpec2 = PKCS8EncodedKeySpec(private)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec2)
    }


}