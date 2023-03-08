package com.example.owllearn.ui.decks

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.DeckEditRecyclerAdapter
import com.example.owllearn.R
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.viewmodel.SharedViewModel
import com.example.owllearn.databinding.FragmentEditDeckBinding


class EditDeckFragment : Fragment() {
    private var _binding: FragmentEditDeckBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null
    private var deckId: String? = null
    private var deckName: String? = null
    private lateinit var adapter: DeckEditRecyclerAdapter
    private lateinit var deckEditRecycler: RecyclerView
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditDeckBinding.inflate(inflater, container, false)
        userId = arguments?.getString("userId", "")
        deckId = arguments?.getString("deckId", "")
        deckName = arguments?.getString("deckName", null)
        if (deckId != "") {
            sharedViewModel.reloadCards(deckId!!)
        }
        binding.createEditTitle.text = String.format(resources.getString(R.string.create_edit_title), deckName)
        binding.cardsEditButton.isClickable = false
        binding.cardsEditButton.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.createEditDeckDisplay.context, R.color.purple))
        if (changed) {
            binding.cardsSaveChanges.visibility = View.VISIBLE
        }
        setupRecyclerView()
        setupCreateButton()
        setupEditButton()
        setupSaveButton()
        return binding.root
    }

    private fun setupCreateButton() {
        val createButton = binding.cardsCreateButton
        createButton.setOnClickListener {
            moveToCardCreate(deckId = deckId!!)
        }

    }

    private fun setupSaveButton() {
        val saveButton = binding.cardsSaveChanges
        saveButton.setOnClickListener {
            sharedViewModel.uploadDeck(deckId!!)
        }
    }

    private fun setupEditButton() {
        val editButton = binding.cardsEditButton
        editButton.setOnClickListener {
            val currCard = adapter.lastSelectedCard
            if (currCard != null) {
                moveToCardCreate(currCard.cardId, deckId!!, currCard.front, currCard.back)
                changed = true
            }
        }

    }

    private fun setupRecyclerView() {
        deckEditRecycler = binding.createEditRecycler
        adapter = DeckEditRecyclerAdapter(binding, ::minusButtonOnClick)
        val gridLayoutManager = LinearLayoutManager(context)
        deckEditRecycler.adapter = adapter
        deckEditRecycler.layoutManager = gridLayoutManager
        sharedViewModel.currDeck.observe(viewLifecycleOwner) {
            binding.editDeckProgress.visibility = View.GONE
            adapter.submitList(it.cards)
        }
    }

    private fun moveToCardCreate(cardId: String? = null, deckId: String, front: String? = null, back: String? = null) {
        changed = true
        when (cardId) {
            null -> {
                val action = EditDeckFragmentDirections.actionDecksEditToCardCreate(
                    deckId = deckId
                )
                findNavController().navigate(action)
            }
            else -> {
                val action = EditDeckFragmentDirections.actionDecksEditToCardCreate(
                    deckId = deckId,
                    cardId = cardId,
                    front = front!!,
                    back = back!!
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun minusButtonOnClick(card: Card) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage("Are you sure you want to delete this card?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                sharedViewModel.deleteCard(card.cardId)
                changed = true

            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }

        val alert = builder.create()
        alert.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var changed = false
    }


}