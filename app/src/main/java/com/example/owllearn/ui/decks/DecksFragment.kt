package com.example.owllearn.ui.decks

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.DecksRecyclerAdapter
import com.example.owllearn.R
import com.example.owllearn.data.viewmodel.SharedViewModel
import com.example.owllearn.databinding.FragmentDecksBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*


class DecksFragment : Fragment() {

    private var _binding: FragmentDecksBinding? = null
    private val binding get() = _binding!!
    private lateinit var deckRecycler: RecyclerView
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var preferences: SharedPreferences
    private var adapter: DecksRecyclerAdapter? = null
    private lateinit var userId: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDecksBinding.inflate(inflater, container, false)
        preferences = requireActivity().getSharedPreferences("PREFERENCE", AppCompatActivity.MODE_PRIVATE)
//        userId = preferences.getString(consts.UID, null)!!
        userId = "user-for-tests"
        deckRecycler = binding.decksRecycler
        setupRecyclerView()
        binding.decksEditButton.isClickable = false
        binding.decksEditButton.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.decksCard.context, R.color.purple))
        binding.decksDeleteButton.isClickable = false
        binding.decksDeleteButton.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.decksCard.context, R.color.purple))
        setupCreateButton()
        setupDeleteButton()
        setupEditButton()
        val root: View = binding.root



        return root
    }

    private fun moveToEditDeck(deckId: String, deckName: String) {
        val action = DecksFragmentDirections.actionDecksToDeckCreateEdit(
            deckId = deckId,
            deckName = deckName,
            userId = userId
        )
        findNavController().navigate(action)

    }

    private fun setupEditButton() {
        val editButton = binding.decksEditButton

        editButton.setOnClickListener {
            val currSelectedDeck = (binding.decksRecycler.adapter as DecksRecyclerAdapter).lastSelectedDeck
            val deckId = currSelectedDeck?.deckId
            val deckName = currSelectedDeck?.deckName

            if (deckId != null && deckName != null) {
                moveToEditDeck(deckId, deckName)
            }
        }
    }

    private fun setupDeleteButton() {
        val deleteButton = binding.decksDeleteButton
        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            builder.setMessage("Are you sure you want to delete this deck?")
                .setCancelable(false)
                .setPositiveButton("Delete") { dialog, id ->
                    val currSelectedDeck = (binding.decksRecycler.adapter as DecksRecyclerAdapter).lastSelectedDeck
                    if (currSelectedDeck != null) {
                        sharedViewModel.deleteDeck(currSelectedDeck.deckId)

                    }
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    dialog.dismiss()
                }

            val alert = builder.create()
            alert.show()


        }
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
                val deckName = input.text.toString()
                val deckId = UUID.randomUUID().toString()
                sharedViewModel.createDeck(userId, deckId, deckName)
                moveToEditDeck(deckId, deckName)

                }


            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()


            dialog.show()
        }

    }


    private fun setupRecyclerView() {
        adapter = DecksRecyclerAdapter(binding)
        val gridLayoutManager = GridLayoutManager(context, 2)
        deckRecycler.adapter = adapter
        deckRecycler.layoutManager = gridLayoutManager
        sharedViewModel.decks.observe(viewLifecycleOwner) {
            binding.decksProgress.visibility = View.GONE
            adapter?.submitList(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}