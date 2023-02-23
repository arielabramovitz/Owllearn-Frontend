package com.example.gallery

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
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
    private lateinit var itemView: View
    private var lastSelectedView: CardView? = null
    var lastSelectedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckPreviewViewHolder {
        val v: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.deck_preview, parent, false)
        itemView = v
        return DeckPreviewViewHolder(v)
    }




    override fun onBindViewHolder(holder: DeckPreviewViewHolder, position: Int) {
        val card =  holder.itemView.findViewById<CardView>(R.id.deck_preview)
        if (position == 0) {
            card.setCardBackgroundColor(ContextCompat.getColor(binding.deckPreviews.context, R.color.selected))
            lastSelectedView = card
        }
        holder.itemView.setOnClickListener {
            card.setCardBackgroundColor(ContextCompat.getColor(binding.deckPreviews.context, R.color.selected))

            lastSelectedView?.setCardBackgroundColor(ContextCompat.getColor(binding.deckPreviews.context, R.color.back))
            lastSelectedView = card
            lastSelectedPos = position
        }
        holder.bind(getItem(position))
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


