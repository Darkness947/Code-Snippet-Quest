package com.vigilante.codesnippetquest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.vigilante.codesnippetquest.MyApplication
import com.vigilante.codesnippetquest.ui.auth.LoginViewModel
import com.vigilante.codesnippetquest.ui.auth.RegisterViewModel
import com.vigilante.codesnippetquest.ui.home.HomeViewModel
import com.vigilante.codesnippetquest.ui.game.GameplayViewModel
import com.vigilante.codesnippetquest.ui.history.HistoryViewModel

class ViewModelFactory(private val application: MyApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val dao = application.container.database.appDao()
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(dao) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(dao) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(dao) as T
            modelClass.isAssignableFrom(GameplayViewModel::class.java) -> GameplayViewModel(dao) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(dao) as T
            modelClass.isAssignableFrom(com.vigilante.codesnippetquest.ui.settings.SettingsViewModel::class.java) -> com.vigilante.codesnippetquest.ui.settings.SettingsViewModel(dao) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
