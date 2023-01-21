package com.example.myfitness.utils

import java.util.regex.Pattern

class Validator {
    companion object {
        fun isEmailValid(email : String) : Boolean {
            var isValid = true
            val expression = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            if (!matcher.matches()) isValid = false
            return isValid
        }

        fun isUsernameValid(username : String) : Boolean {
            if (username.length < 3) return false
            if (username.length > 20) return false

            val expression = "^[a-zA-Z0-9]{2,50}\$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(username)
            if (!matcher.matches()) return false

            return true
        }

        fun isPasswordValid(password : String) : Boolean {
            if (password.length < 6) return false
            return true
        }

        fun isActivityValid(activity : String) : Boolean {
            var isValid = true
            val expression = "^[0-7]$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(activity)
            if (!matcher.matches()) isValid = false
            return isValid

        }
    }
}