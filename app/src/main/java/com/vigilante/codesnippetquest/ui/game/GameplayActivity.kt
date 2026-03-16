package com.vigilante.codesnippetquest.ui.game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vigilante.codesnippetquest.R
import com.vigilante.codesnippetquest.data.DatabaseHelper
import com.vigilante.codesnippetquest.data.Question
import com.vigilante.codesnippetquest.databinding.ActivityGameplayBinding
import java.text.SimpleDateFormat
import java.util.*

class GameplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameplayBinding
    private lateinit var dbHelper: DatabaseHelper
    private var questions: List<Question> = listOf()
    private var currentQuestionIndex = 0
    private var score = 0
    private var selectedAnswer: String? = null
    private var userId: Int = -1
    private var level: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)
        level = intent.getIntExtra("LEVEL", 1)

        questions = dbHelper.getQuestionsForLevel(level)

        if (questions.isNotEmpty()) {
            displayQuestion()
        }

        setupOptionButtons()

        binding.btnSubmit.setOnClickListener {
            checkAnswer()
        }

        binding.btnHint.setOnClickListener {
            Toast.makeText(this, "The correct answer is " + questions[currentQuestionIndex].correct, Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayQuestion() {
        val q = questions[currentQuestionIndex]
        binding.tvLevelInfo.text = "Level $level - Question ${currentQuestionIndex + 1}/${questions.size}"
        binding.pbProgress.progress = ((currentQuestionIndex + 1) * 100) / questions.size
        binding.tvQuestionText.text = q.text
        binding.tvSnippet.text = q.snippet
        binding.btnOptionA.text = q.opA
        binding.btnOptionB.text = q.opB
        binding.btnOptionC.text = q.opC
        binding.btnOptionD.text = q.opD

        resetOptionStyles()
        selectedAnswer = null
    }

    private fun setupOptionButtons() {
        val options = listOf(binding.btnOptionA, binding.btnOptionB, binding.btnOptionC, binding.btnOptionD)
        options.forEachIndexed { index, button ->
            button.setOnClickListener {
                resetOptionStyles()
                button.setBackgroundTintList(getColorStateList(R.color.orange_primary))
                button.setTextColor(getColor(R.color.white))
                selectedAnswer = when (index) {
                    0 -> "A"
                    1 -> "B"
                    2 -> "C"
                    3 -> "D"
                    else -> null
                }
            }
        }
    }

    private fun resetOptionStyles() {
        val options = listOf(binding.btnOptionA, binding.btnOptionB, binding.btnOptionC, binding.btnOptionD)
        options.forEach { button ->
            button.setBackgroundTintList(getColorStateList(R.color.white))
            button.setTextColor(getColor(R.color.black))
        }
    }

    private fun checkAnswer() {
        if (selectedAnswer == null) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedAnswer == questions[currentQuestionIndex].correct) {
            score++
        }

        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            displayQuestion()
        } else {
            finishLevel()
        }
    }

    private fun finishLevel() {
        val percentage = (score * 100) / questions.size
        val status = if (percentage >= 60) "PASS" else "FAIL"
        val date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())

        val levelName = when(level) {
            1 -> "Fundamentals"
            2 -> "Control Flow"
            3 -> "OOP"
            4 -> "Advanced"
            else -> "Unknown"
        }

        dbHelper.addHistoryRecord(userId, levelName, percentage, status, date)

        if (status == "PASS" && level < 4) {
            val currentUnlocked = dbHelper.getUnlockedLevel(userId)
            if (level == currentUnlocked) {
                dbHelper.updateUnlockedLevel(userId, level + 1)
            }
        }

        Toast.makeText(this, "Level Complete! Score: $percentage%", Toast.LENGTH_LONG).show()
        finish()
    }
}
