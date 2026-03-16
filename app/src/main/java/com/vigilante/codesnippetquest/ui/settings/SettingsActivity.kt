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
import com.vigilante.codesnippetquest.ui.home.HomeActivity
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("My_Lang", "en") ?: "en"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = userPrefs.getInt("userId", -1)

        binding.ivBack.setOnClickListener {
            finish()
        }

        // Language Toggle
        val currentLang = prefs.getString("My_Lang", "en") ?: "en"
        binding.switchLanguage.isChecked = currentLang == "ar"
        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            val newLang = if (isChecked) "ar" else "en"
            prefs.edit().putString("My_Lang", newLang).apply()

            // Restart the entire app stack so all activities pick up the new locale
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Dark Mode Toggle
        val isDarkMode = prefs.getBoolean("DarkMode", false)
        binding.switchTheme.isChecked = isDarkMode
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("DarkMode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
                Toast.makeText(this, getString(com.vigilante.codesnippetquest.R.string.history_cleared), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
