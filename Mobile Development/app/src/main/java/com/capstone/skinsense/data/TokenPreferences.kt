package com.capstone.skinsense.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class TokenPreferences(private val context: Context) {

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val LOGIN_TOKEN_KEY = stringPreferencesKey("login_token")
    }

    // Save userId
    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    // Get userId
    val userId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    // Get Token
    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LOGIN_TOKEN_KEY]
    }

    // Save Token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_TOKEN_KEY] = token
        }
    }

    // Clear Token
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(LOGIN_TOKEN_KEY)
        }
    }
}
