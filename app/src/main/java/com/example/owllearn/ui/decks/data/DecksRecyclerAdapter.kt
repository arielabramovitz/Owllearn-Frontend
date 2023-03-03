package com.example.gallery

import android.graphics.Color
import android.graphics.LinearGradient
import android.opengl.Visibility
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.owllearn.R
import com.example.owllearn.databinding.FragmentDecksBinding
import com.example.owllearn.ui.decks.data.model.Deck


class DeckAdapter(private val binding: FragmentDecksBinding) :
    ListAdapter<Deck, DeckAdapter.DeckViewHolder>(DiffCallback()) {

    private lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val v: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.deck_preview, parent, false)
        itemView = v
        return DeckViewHolder(v)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    inner class DeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.deck_preview_text)
        fun bind(item: Deck?) {

            textView.text = item?.deckName
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Deck>() {
        override fun areItemsTheSame(oldItem: Deck, newItem: Deck): Boolean {
            return oldItem.deckId == newItem.deckId
        }

        override fun areContentsTheSame(oldItem: Deck, newItem: Deck): Boolean {
            for (a in oldItem.cards) {
                var found = false
                for (b in newItem.cards) {
                    if (a.cardId == b.cardId) {
                        found = true
                    }
                }
                if (!found) {
                    return false
                }
            }
            return true
        }
    }
}


