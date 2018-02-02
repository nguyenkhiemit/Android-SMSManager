package sms.newgate.com.smseditor.util

import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec
import android.R.attr.data



/**
 * Created by apple on 2/1/18.
 */
class StringCryptor {
    val CIPHER_ALGORITHM: String = "AES"
    val RANDOM_GENERATOR_ALGORITHM: String = "SHA1PRNG"
    val RANDOM_KEY_SIZE: Int = 128

    fun encrypt(password: String, data: String): String {
        var secretKey = generateKey(password.toByteArray())
        val clear = data.toByteArray()
        val secretKeySpec = SecretKeySpec(secretKey, CIPHER_ALGORITHM)
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val enctypted = cipher.doFinal(clear)
        return ""
    }

    fun generateKey(seed: ByteArray): ByteArray {
        val keyGenerator = KeyGenerator.getInstance(CIPHER_ALGORITHM)
        val secureRandom = SecureRandom.getInstance(RANDOM_GENERATOR_ALGORITHM)
        secureRandom.setSeed(seed)
        keyGenerator.init(RANDOM_KEY_SIZE, secureRandom)
        val secretKey = keyGenerator.generateKey()
        return secretKey.encoded
    }

}