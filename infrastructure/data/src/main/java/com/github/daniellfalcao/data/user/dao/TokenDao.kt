package com.github.daniellfalcao.data.user.dao

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TokenDao(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val TOKEN_PREFERENCES_KEY = "TOKEN_PREFERENCES_KEY"
    }

    suspend fun token(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(TOKEN_PREFERENCES_KEY, null)
    }

    suspend fun saveToken(token: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit(commit = true) {
            putString(TOKEN_PREFERENCES_KEY, token)
        }
    }

    suspend fun deleteToken() {
        sharedPreferences.edit(commit = true) {
            putString(TOKEN_PREFERENCES_KEY, null)
        }
    }

}