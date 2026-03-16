package com.vigilante.codesnippetquest.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vigilante.codesnippetquest.data.DatabaseHelper
import com.vigilante.codesnippetquest.databinding.ActivityHomeBinding
import com.vigilante.codesnippetquest.ui.history.HistoryActivity
import com.vigilante.codesnippetquest.ui.settings.SettingsActivity
import com.vigilante.codesnippetquest.ui.game.GameplayActivity
import android.view.View
import com.vigilante.codesnippetquest.R

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
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

    private fun setupLevels() {
        val unlockedLevel = dbHelper.getUnlockedLevel(userId)

        // Level 1
        binding.cardLevel1.setOnClickListener {
            startLevel(1)
        }

        // Level 2
        if (unlockedLevel >= 2) {
            binding.cardLevel2.alpha = 1.0f
            binding.ivAction2.setImageResource(R.drawable.ic_play_circle)
            binding.cardLevel2.setOnClickListener { startLevel(2) }
        }

        // Level 3
        if (unlockedLevel >= 3) {
            binding.cardLevel3.alpha = 1.0f
            binding.ivAction3.setImageResource(R.drawable.ic_play_circle)
            binding.cardLevel3.setOnClickListener { startLevel(3) }
        }

        // Level 4
        if (unlockedLevel >= 4) {
            binding.cardLevel4.alpha = 1.0f
            binding.ivAction4.setImageResource(R.drawable.ic_play_circle)
            binding.cardLevel4.setOnClickListener { startLevel(4) }
        }
    }

    private fun startLevel(level: Int) {
        val intent = Intent(this, GameplayActivity::class.java)
        intent.putExtra("LEVEL", level)
        startActivity(intent)
    }
}
