package com.example.shoppinglist

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Расширение для контекста для инициализации DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeSettings(private val context: Context) {
    
    companion object {
        // Ключ для сохранения булевого значения темы
        val DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
    }

    // Поток (Flow) для чтения темы (по умолчанию false - светлая)
    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] == true
        }

    // Функция для сохранения новой темы
    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isDarkMode
        }
    }
}
