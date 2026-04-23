package com.vigilante.codesnippetquest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vigilante.codesnippetquest.data.AppDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: AppDao) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadUserData(userId: Int) {
        if (userId == -1) return
        
        viewModelScope.launch {
            val username = dao.getUsernameById(userId) ?: "User"
            _uiState.value = _uiState.value.copy(username = username)
            
            dao.getUnlockedLevelFlow(userId).collectLatest { level ->
                _uiState.value = _uiState.value.copy(unlockedLevel = level ?: 1)
            }
        }
    }
}

data class HomeUiState(
    val username: String = "",
    val unlockedLevel: Int = 1
)
