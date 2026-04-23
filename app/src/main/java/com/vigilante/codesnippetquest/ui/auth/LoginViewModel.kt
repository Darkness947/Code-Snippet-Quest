package com.vigilante.codesnippetquest.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vigilante.codesnippetquest.data.AppDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val dao: AppDao) : ViewModel() {
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _loginState.value = LoginState.Error("fill_all_fields")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val user = dao.loginUser(username, password)
            if (user != null) {
                _loginState.value = LoginState.Success(user.id)
            } else {
                _loginState.value = LoginState.Error("invalid_credentials")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userId: Int) : LoginState()
    data class Error(val messageKey: String) : LoginState()
}
