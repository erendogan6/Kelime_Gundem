package com.example.kelimegundem.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kelimegundem.databinding.SearchItemRowBinding
import com.example.kelimegundem.model.SearchHistoryItem

class SearchHistoryAdaptor(private var history: List<SearchHistoryItem>,
                           private val onClick: (String) -> Unit,
                           private val onDeleteClicked: (SearchHistoryItem) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdaptor.ViewHolder>() {

    inner class ViewHolder(private val binding: SearchItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: SearchHistoryItem) {
            binding.textViewQuery.text = historyItem.query
            binding.root.setOnClickListener { onClick(historyItem.query) }
            binding.buttonDelete.setOnClickListener { onDeleteClicked(historyItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = history[position]
        holder.bind(historyItem)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    fun updateHistory(newHistory: List<SearchHistoryItem>) {
        history = newHistory
        notifyDataSetChanged()
    }

}
