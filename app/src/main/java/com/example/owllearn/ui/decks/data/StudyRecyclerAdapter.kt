package com.example.gallery

import android.animation.ObjectAnimator
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
import com.example.owllearn.databinding.FragmentStudyBinding


class StudyRecyclerAdapter(private val binding: FragmentStudyBinding) :
    ListAdapter<Deck, StudyRecyclerAdapter.DeckViewHolder>(DiffCallback()) {

    private var lastSelectedView: CardView? = null
    var lastSelectedDeck: Deck? = null
    var lastSelectedPos = -1
    private lateinit var itemView: View
    private lateinit var buttonColor: ColorStateList
    private lateinit var hintPlaceHolder: String
    var selectedDeckId: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val v: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.deck_preview, parent, false)
        hintPlaceHolder = binding.studySampleSize.hint.toString()
        itemView = v
        buttonColor = binding.studyButtonStart.backgroundTintList!!
        return DeckViewHolder(v)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val card = holder.itemView.findViewById<CardView>(R.id.deck_preview)
        holder.itemView.setOnClickListener {
            if (position == lastSelectedPos) {
                card.setCardBackgroundColor(ContextCompat.getColor(binding.studyDeckCard.context, R.color.back))
                lastSelectedView = null
                lastSelectedPos = -1
                binding.studyButtonStart.isClickable = false
                binding.studyButtonStart.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(binding.studyDeckCard.context, R.color.purple))

                selectedDeckId = null
                fadeOutButton()
                lastSelectedDeck = null

            } else {
                card.setCardBackgroundColor(ContextCompat.getColor(binding.studyDeckCard.context, R.color.selected))
                lastSelectedView?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.studyDeckCard.context,
                        R.color.back
                    )
                )

                lastSelectedView = card
                lastSelectedPos = position
                lastSelectedDeck = holder.deck
                selectedDeckId = holder.deck?.deckId
                binding.studySampleSize.hint = String.format(hintPlaceHolder, holder.deck?.cards?.size)
                fadeInButton()
                binding.studyButtonStart.isClickable = true
                binding.studyButtonStart.backgroundTintList = buttonColor
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

    private fun fadeInButton() {
        val input = ObjectAnimator.ofFloat(binding.studySampleSize, "alpha", 0f, 1f)
        val button = ObjectAnimator.ofFloat(binding.studyButtonStart, "alpha", 0f, 1f)
        input.duration = 1000
        button.duration = 1000
        binding.studySampleSize.visibility = View.VISIBLE
        binding.studyButtonStart.visibility = View.VISIBLE
        input.start()
        button.start()

    }

    private fun fadeOutButton() {
        val input = ObjectAnimator.ofFloat(binding.studySampleSize, "alpha", 1f, 0f)
        val button = ObjectAnimator.ofFloat(binding.studyButtonStart, "alpha", 1f, 0f)
        input.duration = 1000
        button.duration = 1000

        binding.studySampleSize.visibility = View.INVISIBLE
        binding.studyButtonStart.visibility = View.GONE
        input.start()
        button.start()

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


