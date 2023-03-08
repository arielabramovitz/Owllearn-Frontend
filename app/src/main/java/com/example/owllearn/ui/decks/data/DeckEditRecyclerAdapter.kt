package com.example.gallery

import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.owllearn.R
import com.example.owllearn.databinding.FragmentDecksBinding
import com.example.owllearn.databinding.FragmentEditDeckBinding
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.model.Deck


class DeckEditRecyclerAdapter(
    private val binding: FragmentEditDeckBinding,
    private var onClick: (Card) -> Unit
) :
    ListAdapter<Card, DeckEditRecyclerAdapter.CardViewHolder>(DiffCallback()) {

    private var lastSelectedView: CardView? = null
    var lastSelectedCard: Card? = null
    var lastSelectedPos = -1
    private lateinit var itemView: View
    private lateinit var buttonColor: ColorStateList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val v: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_in_deck_edit, parent, false)
        itemView = v
        buttonColor = binding.cardsCreateButton.backgroundTintList!!
        return CardViewHolder(v)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = holder.itemView.findViewById<CardView>(R.id.card_in_list)
        holder.itemView.setOnClickListener {
            if (position == lastSelectedPos) {
                card.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.back))
                lastSelectedView = null
                lastSelectedPos = -1
                binding.cardsEditButton.isClickable = false
                binding.cardsEditButton.backgroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.purple
                        )
                    )

                lastSelectedCard = null

            } else {
                card.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.selected
                    )
                )
                lastSelectedView?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.back
                    )
                )
                lastSelectedView = card
                lastSelectedPos = position
                lastSelectedCard = holder.card
                binding.cardsEditButton.isClickable = true
                binding.cardsEditButton.backgroundTintList = buttonColor
            }
        }
        holder.bind(getItem(position))
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.card_name)
        private val imageButton: ImageButton = itemView.findViewById(R.id.card_remove_button)
        var card: Card? = null
        fun bind(item: Card?) {
            card = item
            textView.text = item?.front
            imageButton.setOnClickListener {
                if (item != null) {
                    onClick(item)
                }
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.cardId == newItem.cardId && oldItem.deckId == newItem.deckId
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {

            return oldItem.front == newItem.front && oldItem.back == newItem.back
        }
    }
}


