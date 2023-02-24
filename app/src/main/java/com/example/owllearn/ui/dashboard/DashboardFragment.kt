package com.example.owllearn.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.DeckPreviewAdapter
import com.example.owllearn.R
import com.example.owllearn.databinding.FragmentDashboardBinding
import com.example.owllearn.ui.dashboard.data.viewmodel.DecksPreviewViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var decksPreviewViewModel: DecksPreviewViewModel
    private lateinit var deckPreviewRecycler: RecyclerView
    private lateinit var deckStatRecycler: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        decksPreviewViewModel = ViewModelProvider(this)[DecksPreviewViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val root: View = binding.root
        deckPreviewRecycler = binding.deckPreviewsRecycler
        deckStatRecycler = binding.deckStatsRecycler
        setupDecksPreviewRecycler()

        return root
    }

    private fun setupDecksPreviewRecycler() {
        val adapter = DeckPreviewAdapter(binding)

        deckPreviewRecycler.adapter = adapter
        deckPreviewRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        decksPreviewViewModel._decks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

