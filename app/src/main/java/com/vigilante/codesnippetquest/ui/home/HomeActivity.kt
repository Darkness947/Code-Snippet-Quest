package com.vigilante.codesnippetquest.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vigilante.codesnippetquest.R
import com.vigilante.codesnippetquest.data.DatabaseHelper
import com.vigilante.codesnippetquest.databinding.ActivityHomeBinding
import com.vigilante.codesnippetquest.ui.history.HistoryActivity
import com.vigilante.codesnippetquest.ui.settings.SettingsActivity
import com.vigilante.codesnippetquest.ui.game.GameplayActivity
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply dark mode preference
        val settingsPrefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val isDark = settingsPrefs.getBoolean("DarkMode", false)
        if (isDark) {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        if (userId == -1) {
            finish()
            return
        }

        // Fix 2: Display real username
        val username = dbHelper.getUsernameById(userId)
        binding.tvWelcome.text = getString(R.string.welcome_user, username)

        setupLevels()

        binding.ivSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnScoreHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        setupLevels()
    }

    override fun attachBaseContext(newBase: android.content.Context) {
        val prefs = newBase.getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        val lang = prefs.getString("My_Lang", "en") ?: "en"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = android.content.res.Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    private fun setupLevels() {
        val unlockedLevel = dbHelper.getUnlockedLevel(userId)

        // Level 1 – always unlocked, always styled active
        binding.cardLevel1.alpha = 1.0f
        binding.ivAction1.setImageResource(R.drawable.ic_play_circle)
        binding.cardLevel1.setOnClickListener { startLevel(1) }

        // Level 2
        if (unlockedLevel >= 2) {
            unlockCard2()
            binding.cardLevel2.setOnClickListener { startLevel(2) }
        } else {
            lockCard2()
            binding.cardLevel2.setOnClickListener {
                Toast.makeText(this, getString(R.string.level_locked), Toast.LENGTH_SHORT).show()
            }
        }

        // Level 3
        if (unlockedLevel >= 3) {
            unlockCard3()
            binding.cardLevel3.setOnClickListener { startLevel(3) }
        } else {
            lockCard3()
            binding.cardLevel3.setOnClickListener {
                Toast.makeText(this, getString(R.string.level_locked), Toast.LENGTH_SHORT).show()
            }
        }

        // Level 4
        if (unlockedLevel >= 4) {
            unlockCard4()
            binding.cardLevel4.setOnClickListener { startLevel(4) }
        } else {
            lockCard4()
            binding.cardLevel4.setOnClickListener {
                Toast.makeText(this, getString(R.string.level_locked), Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ── Unlock helpers: same look as Level 1 ──────────────────────────

    private fun unlockCard2() {
        binding.cardLevel2.alpha = 1.0f
        binding.ivAction2.setImageResource(R.drawable.ic_play_circle)
        binding.tvLevel2Label.setTextColor(getColor(R.color.level_active_label))
        binding.tvLevel2Name.setTextColor(getColor(R.color.text_primary))
        binding.tvLevel2Subtitle.setTextColor(getColor(R.color.text_secondary))
    }

    private fun unlockCard3() {
        binding.cardLevel3.alpha = 1.0f
        binding.ivAction3.setImageResource(R.drawable.ic_play_circle)
        binding.tvLevel3Label.setTextColor(getColor(R.color.level_active_label))
        binding.tvLevel3Name.setTextColor(getColor(R.color.text_primary))
        binding.tvLevel3Subtitle.setTextColor(getColor(R.color.text_secondary))
    }

    private fun unlockCard4() {
        binding.cardLevel4.alpha = 1.0f
        binding.ivAction4.setImageResource(R.drawable.ic_play_circle)
        binding.tvLevel4Label.setTextColor(getColor(R.color.level_active_label))
        binding.tvLevel4Name.setTextColor(getColor(R.color.text_primary))
        binding.tvLevel4Subtitle.setTextColor(getColor(R.color.text_secondary))
    }

    // ── Lock helpers: grayed out ──────────────────────────────────────

    private fun lockCard2() {
        binding.cardLevel2.alpha = 0.5f
        binding.ivAction2.setImageResource(R.drawable.ic_lock)
        binding.tvLevel2Label.setTextColor(getColor(R.color.level_locked_text))
        binding.tvLevel2Name.setTextColor(getColor(R.color.level_locked_text))
        binding.tvLevel2Subtitle.setTextColor(getColor(R.color.level_locked_text))
    }

    private fun lockCard3() {
        binding.cardLevel3.alpha = 0.5f
        binding.ivAction3.setImageResource(R.drawable.ic_lock)
        binding.tvLevel3Label.setTextColor(getColor(R.color.level_locked_text))
        binding.tvLevel3Name.setTextColor(getColor(R.color.level_locked_text))
        binding.tvLevel3Subtitle.setTextColor(getColor(R.color.level_locked_text))
    }

    private fun lockCard4() {
        binding.cardLevel4.alpha = 0.5f
        binding.ivAction4.setImageResource(R.drawable.ic_lock)
        binding.tvLevel4Label.setTextColor(getColor(R.color.level_locked_text))
        binding.tvLevel4Name.setTextColor(getColor(R.color.level_locked_text))
        binding.tvLevel4Subtitle.setTextColor(getColor(R.color.level_locked_text))
    }

    private fun startLevel(level: Int) {
        val intent = Intent(this, GameplayActivity::class.java)
        intent.putExtra("LEVEL", level)
        startActivity(intent)
    }
}
