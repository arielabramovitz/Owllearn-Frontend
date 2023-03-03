package com.example.owllearn.ui.dashboard

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.DeckPreviewAdapter
import com.example.owllearn.R
import com.example.owllearn.databinding.FragmentDashboardBinding
import com.example.owllearn.ui.dashboard.data.viewmodel.DecksPreviewViewModel
import com.example.owllearn.util.consts

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var decksPreviewViewModel: DecksPreviewViewModel
    private lateinit var deckPreviewRecycler: RecyclerView
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        decksPreviewViewModel = DecksPreviewViewModel(requireActivity().application)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        preferences = requireActivity().getSharedPreferences("PREFERENCE", AppCompatActivity.MODE_PRIVATE)

        val userFirstName = preferences.getString(consts.FIRST_NAME, getString(R.string.username))

        binding.dashboardTitle.text = String.format(resources.getString(R.string.title), userFirstName)

        deckPreviewRecycler = binding.deckPreviewsRecycler
        setupDecksPreviewRecycler()
        disableBack()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userFirstName = preferences.getString(consts.FIRST_NAME, getString(R.string.username))
        binding.dashboardTitle.text = String.format(resources.getString(R.string.title), userFirstName)
        binding.root.findViewById<DrawerLayout>(R.id.drawer_layout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    }

    private fun disableBack() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }
        )
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

