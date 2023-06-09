package com.example.owllearn.ui.dashboard

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.DeckPreviewAdapter
import com.example.owllearn.R
import com.example.owllearn.data.viewmodel.SharedViewModel
import com.example.owllearn.databinding.FragmentDashboardBinding
import com.example.owllearn.util.consts

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel  by activityViewModels()
    private lateinit var deckPreviewRecycler: RecyclerView
    private lateinit var preferences: SharedPreferences
    private lateinit var userId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        preferences = requireActivity().getSharedPreferences("PREFERENCE", AppCompatActivity.MODE_PRIVATE)
        userId = preferences.getString(consts.UID, arguments?.getString("userId")).toString()
        sharedViewModel.reloadDeckPreviews(userId)
        sharedViewModel.reloadDecks(userId)


        val userFirstName = arguments?.getString(consts.FIRST_NAME, "user")


        binding.dashboardTitle.text = String.format(resources.getString(R.string.title), userFirstName)

        val root: View = binding.root
        deckPreviewRecycler = binding.deckPreviewsRecycler
        setupDecksPreviewRecycler()
        disableBack()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userFirstName = preferences.getString(consts.FIRST_NAME, getString(R.string.username))
        sharedViewModel.reloadDeckPreviews(userId)

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
        sharedViewModel.previews.observe(viewLifecycleOwner) {
            binding.previewProgress.visibility = View.GONE
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

