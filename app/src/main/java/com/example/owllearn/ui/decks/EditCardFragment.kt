package com.example.owllearn.ui.decks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.owllearn.R
import com.example.owllearn.databinding.CardInDeckEditBinding
import com.example.owllearn.databinding.FragmentEditCardBinding
import com.example.owllearn.databinding.FragmentEditDeckBinding
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.viewmodel.SharedViewModel
import com.example.owllearn.ui.decks.data.provider.CardsProvider
import com.example.owllearn.util.consts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class EditCardFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentEditCardBinding? = null
    private val binding get() = _binding!!
    private lateinit var cardProvider: CardsProvider
    private var cardId: String? = null
    private var deckId: String? = null
    private var front: String? = null
    private var back: String? = null
    private var isCreate = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditCardBinding.inflate(inflater, container, false)
        cardId = arguments?.getString("cardId", null)
        deckId = arguments?.getString("deckId", null)
        front = arguments?.getString("front", null)
        back = arguments?.getString("back", null)
        val preferences = requireContext().getSharedPreferences("PREFERENCE", AppCompatActivity.MODE_PRIVATE)
//        val userId = preferences.getString(consts.UID, "")
        val userId = "user-for-tests"
        cardProvider = CardsProvider(userId, requireContext())
        if (cardId != "null") {
            isCreate = false
            binding.cardCreationFrontEdittext.setText(front)
            binding.cardCreationBackEdittext.setText(back)
            binding.cardCreationCreateButton.text = "Save"
        } else {
            binding.cardCreationFrontEdittext.setText("")
            binding.cardCreationBackEdittext.setText("")
            cardId = UUID.randomUUID().toString()
        }
        setupSaveButton()
        return binding.root
    }

    private fun moveToEdit() {
        findNavController().navigateUp()
    }

    private fun setupSaveButton() {
        val saveButton = binding.cardCreationCreateButton
        saveButton.setOnClickListener {
            front = binding.cardCreationFrontEdittext.text.toString()
            back = binding.cardCreationBackEdittext.text.toString()
            if (isCreate) {
                sharedViewModel.createCard(deckId!!, cardId!!, front!!, back!!)
            } else {
                sharedViewModel.editCard(deckId!!, cardId!!, front!!, back!!)
            }

            moveToEdit()

        }
    }

}