package com.example.owllearn.ui.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.StudyRecyclerAdapter
import com.example.owllearn.data.viewmodel.SharedViewModel
import com.example.owllearn.databinding.FragmentStudyBinding
import com.example.owllearn.util.consts

class StudySetupFragment : Fragment() {

    private lateinit var adapter: StudyRecyclerAdapter
    private var _binding: FragmentStudyBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val preferences = requireActivity().getSharedPreferences("PREFERENCE", AppCompatActivity.MODE_PRIVATE)
        val userId = preferences.getString(consts.UID, null)

        _binding = FragmentStudyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupRecycler()
        setStartButton()
        return root
    }

    private fun setupRecycler() {
        val recyclerView = binding.studyRecycler
        adapter = StudyRecyclerAdapter(binding)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = gridLayoutManager

        sharedViewModel.decks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

    }

    private fun setStartButton() {
        val startButton = binding.studyButtonStart
        startButton.setOnClickListener {
            if (adapter.lastSelectedDeck != null) {
                val batchSizeText = binding.navStudyBatchEdittext.text
                if (batchSizeText!!.isNotEmpty()) {

                    val batchSize = Integer.parseInt(batchSizeText.toString())
                    if (batchSize <= adapter.lastSelectedDeck!!.cards.size) {
                        sharedViewModel.reloadCards(adapter.selectedDeckId!!)
                        val action = StudySetupFragmentDirections.actionStudyToStudyBatch(batchSize)
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Batch size should be smaller than the number of cards in the deck",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}