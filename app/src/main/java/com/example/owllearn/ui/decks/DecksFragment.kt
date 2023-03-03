package com.example.owllearn.ui.decks

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.DeckAdapter
import com.example.owllearn.databinding.FragmentDecksBinding
import com.example.owllearn.ui.decks.data.model.Deck
import com.example.owllearn.ui.decks.data.provider.DecksProvider
import com.example.owllearn.ui.decks.data.viewmodel.DecksViewModel
import com.example.owllearn.util.consts
import java.util.UUID


class DecksFragment : Fragment() {

    private var _binding: FragmentDecksBinding? = null
    private val binding get() = _binding!!
    private lateinit var deckRecycler: RecyclerView
    private lateinit var decksViewModel: DecksViewModel
    private lateinit var preferences: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDecksBinding.inflate(inflater, container, false)
        preferences = requireActivity().getSharedPreferences("PREFERENCE", AppCompatActivity.MODE_PRIVATE)
        val uid = preferences.getString(consts.UID, null)
        decksViewModel = DecksViewModel(DecksProvider(), uid!!)
        deckRecycler = binding.decksRecycler
        setupRecyclerView()
        setupCreateButton()
        val root: View = binding.root



        return root
    }

    private fun setupCreateButton() {
        val createButton = binding.decksCreateButton

        createButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Create New Deck")
            builder.setMessage("Enter the name of the new deck:")

            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("Create") { dialog, which ->
                val itemName = input.text.toString()
                val newDeck = Deck(itemName, UUID.randomUUID().toString(), emptyList())
                decksViewModel.addDeck(newDeck)
                decksViewModel.reloadDecks()
                // TODO: update database with new empty deck (maybe async)
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()


            dialog.show()
        }

    }

    private fun setupRecyclerView() {
        val adapter = DeckAdapter(binding)
        val gridLayoutManager = GridLayoutManager(context, 2)
        deckRecycler.adapter = adapter
        deckRecycler.layoutManager = gridLayoutManager
        decksViewModel.decks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}