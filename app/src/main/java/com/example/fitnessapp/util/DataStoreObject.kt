package com.example.fitnessapp.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DataStoreObject(context: Context) {

    companion object {
        private val TIME = longPreferencesKey("time")
        private val Context.dataStore by preferencesDataStore(name = "settings")

        @Volatile
        private var instance: DataStoreObject? = null
        private val lock = Any()

        operator fun invoke(context: Context): DataStoreObject = instance ?: synchronized(lock) {
            instance ?: DataStoreObject(context).also {
                instance = it
            }
        }
    }

    private val dataStore = context.dataStore

    fun saveTime(zaman: Long) = runBlocking {
        dataStore.edit { preferences ->
            preferences[TIME] = zaman
        }
    }

    fun getTime(): Long = runBlocking {
        val preferences = dataStore.data.first()
        preferences[TIME] ?: 0L
    }
}