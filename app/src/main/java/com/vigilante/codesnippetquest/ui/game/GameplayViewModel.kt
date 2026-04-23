package com.vigilante.codesnippetquest.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vigilante.codesnippetquest.data.AppDao
import com.vigilante.codesnippetquest.data.HistoryRecord
import com.vigilante.codesnippetquest.data.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameplayViewModel(private val dao: AppDao) : ViewModel() {

    private val _uiState = MutableStateFlow(GameplayUiState())
    val uiState: StateFlow<GameplayUiState> = _uiState
    
    private var userId: Int = -1

    fun loadLevel(userId: Int, level: Int) {
        this.userId = userId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, level = level)
            val questions = dao.getQuestionsForLevel(level)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                questions = questions,
                currentQuestionIndex = 0,
                score = 0,
                selectedAnswer = null,
                isFinished = false
            )
        }
    }

    fun selectAnswer(answer: String) {
        _uiState.value = _uiState.value.copy(selectedAnswer = answer)
    }

    fun checkAnswerAndProceed() {
        val currentState = _uiState.value
        if (currentState.selectedAnswer == null) return
        
        val currentQuestion = currentState.questions[currentState.currentQuestionIndex]
        val newScore = if (currentState.selectedAnswer == currentQuestion.correct) {
            currentState.score + 1
        } else {
            currentState.score
        }

        if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
            _uiState.value = currentState.copy(
                score = newScore,
                currentQuestionIndex = currentState.currentQuestionIndex + 1,
                selectedAnswer = null
            )
        } else {
            finishLevel(newScore, currentState.questions.size, currentState.level)
        }
    }

    private fun finishLevel(score: Int, totalQuestions: Int, level: Int) {
        val percentage = if (totalQuestions > 0) (score * 100) / totalQuestions else 0
        val status = if (percentage >= 60) "PASS" else "FAIL"
        val date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())

        val levelName = when(level) {
            1 -> "Fundamentals"
            2 -> "Control Flow"
            3 -> "OOP"
            4 -> "Advanced"
            else -> "Unknown"
        }

        viewModelScope.launch {
            dao.insertHistoryRecord(
                HistoryRecord(
                    userId = userId,
                    levelName = levelName,
                    levelNumber = level,
                    score = percentage,
                    status = status,
                    date = date
                )
            )

            if (status == "PASS" && level < 4) {
                val currentUnlocked = dao.getUnlockedLevel(userId) ?: 1
                if (level == currentUnlocked) {
                    dao.updateUnlockedLevel(userId, level + 1)
                }
            }

            _uiState.value = _uiState.value.copy(
                score = score,
                isFinished = true,
                finalPercentage = percentage
            )
        }
    }
}

data class GameplayUiState(
    val isLoading: Boolean = true,
    val level: Int = 1,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isFinished: Boolean = false,
    val finalPercentage: Int = 0
)
