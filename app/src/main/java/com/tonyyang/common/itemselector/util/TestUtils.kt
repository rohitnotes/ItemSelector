package com.tonyyang.common.itemselector.util

import java.security.SecureRandom


class TestUtils {
    companion object {
        fun getRandomLetter(length: Int): String {
            val secureRandom = SecureRandom()
            val s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
            val generatedString = StringBuilder()
            for (i in 0 until length) {
                val randomSequence = secureRandom.nextInt(s.length)
                generatedString.append(s[randomSequence])
            }
            return generatedString.toString().toLowerCase()
        }
    }
}