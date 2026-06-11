package com.example.shoppinglist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

data class ShoppingItem(
    val id: Long = System.currentTimeMillis() + (0..1000).random(),
    val name: String,
    val isChecked: Boolean = false
)

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {
    
    private val themeSettings = ThemeSettings(application)

    val isDarkMode: StateFlow<Boolean> = themeSettings.isDarkModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _shoppingList = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val shoppingList: StateFlow<List<ShoppingItem>> = _shoppingList.asStateFlow()

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            themeSettings.saveThemeSetting(isDark)
        }
    }

    fun addItem(name: String) {
        if (name.isNotBlank()) {
            _shoppingList.value = _shoppingList.value + ShoppingItem(name = name.trim())
        }
    }

    fun removeItem(item: ShoppingItem) {
        _shoppingList.value = _shoppingList.value - item
    }

    fun toggleItemCheck(item: ShoppingItem) {
        _shoppingList.value = _shoppingList.value.map {
            if (it.id == item.id) it.copy(isChecked = !it.isChecked) else it
        }
    }
}
