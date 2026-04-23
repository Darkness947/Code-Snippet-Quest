package com.vigilante.codesnippetquest.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vigilante.codesnippetquest.data.AppDao
import kotlinx.coroutines.launch

class SettingsViewModel(private val dao: AppDao) : ViewModel() {
    fun clearHistory(userId: Int) {
        if (userId != -1) {
            viewModelScope.launch {
                dao.clearHistory(userId)
            }
        }
    }
}
