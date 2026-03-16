package com.vigilante.codesnippetquest.ui.settings

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.vigilante.codesnippetquest.data.DatabaseHelper
import com.vigilante.codesnippetquest.databinding.ActivitySettingsBinding
import com.vigilante.codesnippetquest.ui.auth.LoginActivity
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Load language preference before setting content view
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = prefs.getString("My_Lang", "en") ?: "en"
        setLocale(language)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        val userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = userPrefs.getInt("userId", -1)

        binding.ivBack.setOnClickListener {
            finish()
        }

        // Language Toggle
        binding.switchLanguage.isChecked = language == "ar"
        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            val newLang = if (isChecked) "ar" else "en"
            setLocale(newLang)
            prefs.edit().putString("My_Lang", newLang).apply()
            
            // Restart activity to apply changes
            val intent = intent
            finish()
            startActivity(intent)
        }

        // Dark Mode Toggle
        val isDarkMode = prefs.getBoolean("DarkMode", false)
        binding.switchTheme.isChecked = isDarkMode
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefs.edit().putBoolean("DarkMode", true).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefs.edit().putBoolean("DarkMode", false).apply()
            }
        }

        // Logout
        binding.rlLogout.setOnClickListener {
            userPrefs.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Clear History
        binding.tvClearHistory.setOnClickListener {
            if (userId != -1) {
                dbHelper.clearHistory(userId)
                Toast.makeText(this, "History Cleared", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}
