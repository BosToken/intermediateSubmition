package com.example.intermediatesubmition.data.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "users")
class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val USER_KEY = stringPreferencesKey("token")

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = token
        }
    }

    fun getToken(): Flow<String>{
        return dataStore.data.map {pref ->
            pref[USER_KEY] ?: "null"
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}