package com.example.owllearn.ui.study

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.StudyRecyclerAdapter
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.viewmodel.SharedViewModel
import com.example.owllearn.databinding.FragmentStudyBatchBinding
import com.example.owllearn.databinding.FragmentStudyBinding
import com.example.owllearn.util.consts

class StudyBatchFragment : Fragment() {

    private var _binding: FragmentStudyBatchBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var cardCount: Int = 0
    private var isFront = true
    private lateinit var batch: List<Card>
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val preferences = requireActivity().getSharedPreferences("PREFERENCE", AppCompatActivity.MODE_PRIVATE)
        _binding = FragmentStudyBatchBinding.inflate(inflater, container, false)
        
        val userId = preferences.getString(consts.UID, null)
        val batchSize = arguments?.getInt("batchSize", 0)
        batch = getBatch(batchSize!!)!!
        val firstCard = batch[0]
        binding.studyCardText.text = firstCard.front
        setupCard()
        val root: View = binding.root

        return root
    }


    private fun getBatch(batchSize: Int): List<Card>? {
        return sharedViewModel.cards.value?.asSequence()?.shuffled()?.take(batchSize)?.toList()

    }

    private fun flipCard() {
        val card = binding.studyCard

        val flipOut = ObjectAnimator.ofFloat(card, "rotationY", 0f, 90f)
        flipOut.duration = 500

        val flipIn = ObjectAnimator.ofFloat(card, "rotationY", 270f, 360f)
        flipIn.duration = 500

        val set = AnimatorSet()
        set.playSequentially(flipOut, flipIn)
        set.start()
    }

    private fun moveToDashboard() {
        val action = StudyBatchFragmentDirections.actionStudyBatchToDashboard()
        findNavController().navigate(action)
    }

    private fun moveToStudy() {
        val action = StudyBatchFragmentDirections.actionStudyBatchToStudy()
        findNavController().navigate(action)
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage("The session is over, should we move back to the dashboard?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                moveToDashboard()

            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
                moveToStudy()
            }

        val alert = builder.create()
        alert.show()
    }


    private fun setupCard() {
        val card = binding.studyCard

        card.setOnClickListener {
            var currCard = batch[cardCount]
            if (isFront) {
                // show back
                flipCard()
                binding.studyCardText.text = currCard.back
                isFront = false
            } else {
                cardCount += 1
                if (cardCount == batch.size) {
                    showDialog()
                } else {
                    // show new front
                    flipCard()
                    currCard = batch[cardCount]
                    binding.studyCardText.text = currCard.back
                    isFront = true
                }

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}