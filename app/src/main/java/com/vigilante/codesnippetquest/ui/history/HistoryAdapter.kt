package com.vigilante.codesnippetquest.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vigilante.codesnippetquest.R
import com.vigilante.codesnippetquest.data.HistoryRecord
import com.vigilante.codesnippetquest.databinding.ItemHistoryBinding
import java.util.Locale

class HistoryAdapter(private val records: List<HistoryRecord>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val record = records[position]
        val ctx = holder.binding.root.context

        // Fix 6: show the correct level number (not always "LEVEL 1")
        holder.binding.tvHistoryLevel.text = ctx.getString(
            when (record.levelNumber) {
                1 -> R.string.level_1
                2 -> R.string.level_2
                3 -> R.string.level_3
                4 -> R.string.level_4
                else -> R.string.level_1
            }
        ).uppercase()

        // Fix: translate the level name when in Arabic
        val isArabic = Locale.getDefault().language == "ar"
        holder.binding.tvHistoryLevelName.text = if (isArabic) {
            when (record.levelNumber) {
                1 -> ctx.getString(R.string.fundamentals)
                2 -> ctx.getString(R.string.control_flow)
                3 -> ctx.getString(R.string.oop)
                4 -> ctx.getString(R.string.advanced)
                else -> record.levelName
            }
        } else {
            record.levelName
        }

        holder.binding.tvHistoryScore.text = "${record.score}% ${ctx.getString(R.string.score)}"
        holder.binding.pbHistoryScore.progress = record.score
        holder.binding.tvHistoryStatus.text = if (isArabic) {
            ctx.getString(if (record.status == "PASS") R.string.pass else R.string.fail)
        } else {
            record.status
        }
        holder.binding.tvHistoryDate.text = record.date

        if (record.status == "PASS") {
            holder.binding.tvHistoryStatus.setBackgroundResource(R.drawable.bg_status_pass)
        } else {
            holder.binding.tvHistoryStatus.setBackgroundResource(R.drawable.bg_status_fail)
        }
    }

    override fun getItemCount() = records.size
}
