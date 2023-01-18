package com.example.myfitness.utils

import java.security.MessageDigest

class Hash {
    companion object {
        fun hashPassword(password: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = password.toByteArray()
            digest.update(bytes, 0, bytes.size)
            val hashedPassword = digest.digest()

            return hashedPassword.toHex()
        }

        fun ByteArray.toHex() : String {
            return joinToString("") { "%02x".format(it) }
        }
    }
}