package com.vigilante.codesnippetquest.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vigilante.codesnippetquest.data.AppDao
import com.vigilante.codesnippetquest.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val dao: AppDao) : ViewModel() {
    
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _registerState.value = RegisterState.Error("fill_all_fields")
            return
        }
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt())
            val userId = dao.insertUser(User(username = username, password = hashedPassword))
            if (userId != -1L) {
                _registerState.value = RegisterState.Success
            } else {
                _registerState.value = RegisterState.Error("registration_failed")
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val messageKey: String) : RegisterState()
}
