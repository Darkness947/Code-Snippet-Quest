package com.vigilante.codesnippetquest.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vigilante.codesnippetquest.R
import com.vigilante.codesnippetquest.data.HistoryRecord
import com.vigilante.codesnippetquest.databinding.ItemHistoryBinding

class HistoryAdapter(private val records: List<HistoryRecord>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val record = records[position]
        holder.binding.tvHistoryLevelName.text = record.levelName
        holder.binding.tvHistoryScore.text = "${record.score}% Score"
        holder.binding.pbHistoryScore.progress = record.score
        holder.binding.tvHistoryStatus.text = record.status
        holder.binding.tvHistoryDate.text = record.date

        if (record.status == "PASS") {
            holder.binding.tvHistoryStatus.setBackgroundResource(R.drawable.bg_status_pass)
        } else {
            holder.binding.tvHistoryStatus.setBackgroundResource(R.drawable.bg_status_fail)
        }
    }

    override fun getItemCount() = records.size
}
