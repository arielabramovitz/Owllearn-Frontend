package com.example.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.owllearn.R
import com.example.owllearn.databinding.DeckPreviewBinding
import com.example.owllearn.databinding.FragmentDashboardBinding
import com.example.owllearn.ui.dashboard.data.model.DeckPreview

class DeckPreviewAdapter (private val binding: FragmentDashboardBinding):
    ListAdapter<DeckPreview, DeckPreviewAdapter.DeckPreviewViewHolder>(DiffCallback()) {
    var onClick: ((DeckPreview) -> Unit)? = null
    private lateinit var itemView: View
    var currSelectedPos = -1
    var lastSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckPreviewViewHolder {
        val v: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.deck_preview, parent, false)
        itemView = v
        return DeckPreviewViewHolder(v)
    }

    private fun selectViewHolder() {
        binding.deckPreviewsRecycler.setBackgroundColor(ContextCompat.getColor(binding.deckPreviews.context, R.color.selected))
    }

    private fun unselectViewHolder() {
        binding.deckPreviewsRecycler.setBackgroundColor(ContextCompat.getColor(binding.deckPreviews.context, R.color.not_selected))

    }


    override fun onBindViewHolder(holder: DeckPreviewViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onClick?.invoke(getItem(position))
        }
        itemView.setOnClickListener {
            currSelectedPos = position
            if(lastSelectedPos == -1)
                lastSelectedPos = currSelectedPos
            else {
                notifyItemChanged(lastSelectedPos)
                lastSelectedPos = currSelectedPos
            }
            notifyItemChanged(currSelectedPos)
        }
    }

    inner class DeckPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.deck_preview_text)
        fun bind(item: DeckPreview?) {
            textView.text = item?.deckName
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<DeckPreview>() {
        override fun areItemsTheSame(oldItem: DeckPreview, newItem: DeckPreview): Boolean {
            return oldItem.deckName == newItem.deckName
        }

        override fun areContentsTheSame(oldItem: DeckPreview, newItem: DeckPreview): Boolean {
            return oldItem == newItem
        }
    }
}


