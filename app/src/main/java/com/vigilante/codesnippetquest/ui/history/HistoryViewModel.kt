package com.vigilante.codesnippetquest.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vigilante.codesnippetquest.data.AppDao
import com.vigilante.codesnippetquest.data.HistoryRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(private val dao: AppDao) : ViewModel() {
    
    fun getHistoryRecords(userId: Int): StateFlow<List<HistoryRecord>> {
        return dao.getHistoryRecords(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
}
