package com.vigilante.codesnippetquest.ui.game

import android.content.Intent
import android.os.Bundle
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

        // Apply dark mode preference
        val settingsPrefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val isDark = settingsPrefs.getBoolean("DarkMode", false)
        if (isDark) {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)
        }

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
            if (questions.isNotEmpty()) {
                Toast.makeText(this, questions[currentQuestionIndex].hint, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun attachBaseContext(newBase: android.content.Context) {
        val prefs = newBase.getSharedPreferences("Settings", MODE_PRIVATE)
        val lang = prefs.getString("My_Lang", "en") ?: "en"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = android.content.res.Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    private fun displayQuestion() {
        val q = questions[currentQuestionIndex]
        // Localized "Level X - Question Y/Z" header
        binding.tvLevelInfo.text = getString(
            R.string.level_question_info,
            level,
            currentQuestionIndex + 1,
            questions.size
        )
        binding.pbProgress.progress = ((currentQuestionIndex + 1) * 100) / questions.size
        // Show Arabic question text when Arabic locale is active
        val isArabic = Locale.getDefault().language == "ar"
        binding.tvQuestionText.text = if (isArabic) q.textAr else q.text
        binding.tvSnippet.text = q.snippet
        // Fix 4: prefix options with A/B/C/D labels
        binding.btnOptionA.text = "A.  ${q.opA}"
        binding.btnOptionB.text = "B.  ${q.opB}"
        binding.btnOptionC.text = "C.  ${q.opC}"
        binding.btnOptionD.text = "D.  ${q.opD}"

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
            button.setBackgroundTintList(getColorStateList(R.color.option_bg))
            button.setTextColor(getColor(R.color.option_text))
        }
    }

    private fun checkAnswer() {
        if (selectedAnswer == null) {
            Toast.makeText(this, getString(R.string.please_select_answer), Toast.LENGTH_SHORT).show()
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

        // Fix 6: pass level number to history record
        dbHelper.addHistoryRecord(userId, levelName, level, percentage, status, date)

        if (status == "PASS" && level < 4) {
            val currentUnlocked = dbHelper.getUnlockedLevel(userId)
            if (level == currentUnlocked) {
                dbHelper.updateUnlockedLevel(userId, level + 1)
            }
        }

        Toast.makeText(this, getString(R.string.level_complete_score, percentage), Toast.LENGTH_LONG).show()
        finish()
    }
}
