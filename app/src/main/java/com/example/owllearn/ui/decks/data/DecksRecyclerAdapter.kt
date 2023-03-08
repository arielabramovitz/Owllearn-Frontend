package com.example.gallery

import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.owllearn.R
import com.example.owllearn.databinding.FragmentDecksBinding
import com.example.owllearn.data.model.Deck


class DecksRecyclerAdapter(private val binding: FragmentDecksBinding) :
    ListAdapter<Deck, DecksRecyclerAdapter.DeckViewHolder>(DiffCallback()) {

    private var lastSelectedView: CardView? = null
    var lastSelectedDeck: Deck? = null
    var lastSelectedPos = -1
    private lateinit var itemView: View
    private lateinit var buttonColor: ColorStateList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val v: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.deck_preview, parent, false)
        itemView = v
        buttonColor = binding.decksCreateButton.backgroundTintList!!
        return DeckViewHolder(v)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val card = holder.itemView.findViewById<CardView>(R.id.deck_preview)
        holder.itemView.setOnClickListener {
            if (position == lastSelectedPos) {
                card.setCardBackgroundColor(ContextCompat.getColor(binding.decksCard.context, R.color.back))
                lastSelectedView = null
                lastSelectedPos = -1
                binding.decksEditButton.isClickable = false
                binding.decksEditButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.decksCard.context, R.color.purple))
                binding.decksDeleteButton.isClickable = false
                binding.decksDeleteButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.decksCard.context, R.color.purple))
                lastSelectedDeck = null

            } else {
                card.setCardBackgroundColor(ContextCompat.getColor(binding.decksCard.context, R.color.selected))
                lastSelectedView?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.decksCard.context,
                        R.color.back
                    )
                )
                lastSelectedView = card
                lastSelectedPos = position
                lastSelectedDeck = holder.deck
                binding.decksEditButton.isClickable = true
                binding.decksEditButton.backgroundTintList = buttonColor
                binding.decksDeleteButton.isClickable = true
                binding.decksDeleteButton.backgroundTintList = buttonColor
            }
        }
        holder.bind(getItem(position))
    }

    inner class DeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.deck_preview_text)
        var deck: Deck? = null
        fun bind(item: Deck?) {
            deck = item
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


