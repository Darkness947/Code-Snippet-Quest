package com.vigilante.codesnippetquest.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vigilante.codesnippetquest.data.DatabaseHelper
import com.vigilante.codesnippetquest.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)

        binding.ivBack.setOnClickListener {
            finish()
        }

        if (userId != -1) {
            val records = dbHelper.getHistoryRecords(userId)
            binding.rvHistory.layoutManager = LinearLayoutManager(this)
            binding.rvHistory.adapter = HistoryAdapter(records)
        }
    }
}
