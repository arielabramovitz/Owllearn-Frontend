package com.example.owllearn.ui.study

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
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
        val deckName = sharedViewModel.currDeck.value?.deckName
        binding.studyBatchText.text = String.format(binding.studyBatchText.text.toString(), deckName)
        val batchSize = arguments?.getInt("batchSize", 0)
        batch = getBatch(batchSize!!)!!
        val firstCard = batch[0]
        binding.studyCardText.text = firstCard.front
        binding.studyCard.setOnClickListener{
            onClick()
        }
        binding.easyButton.setOnClickListener{
            onClick()
        }
        binding.mediumButton.setOnClickListener{
            onClick()
        }
        binding.hardButton.setOnClickListener{
            onClick()
        }

        binding.easyButton.visibility = View.INVISIBLE
        binding.mediumButton.visibility = View.INVISIBLE
        binding.hardButton.visibility = View.INVISIBLE
        binding.easyButton.isClickable = false
        binding.easyButton.isClickable = false
        binding.easyButton.isClickable = false


        val root: View = binding.root

        return root
    }

    private fun setupEasy() {
        val easyButton = binding.easyButton
        easyButton.setOnClickListener {

        }
    }

    private fun setupMedium() {
        val mediumButton = binding.mediumButton

    }

    private fun setupHard() {
        val hardButton = binding.hardButton

    }


    private fun getBatch(batchSize: Int): List<Card>? {
        return sharedViewModel.cards.value?.asSequence()?.shuffled()?.take(batchSize)?.toList()

    }

    private fun flipCard(otherSideText: String) {
        val card = binding.studyCard
        val text = binding.studyCardText

        val flipOut = AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(card, "rotationY", 0f, 90f)).with(
                ObjectAnimator.ofFloat(card, "scaleX", 1f, 0.5f)
            ).with(
                ObjectAnimator.ofFloat(card, "scaleY", 1f, 0.5f)
            ).with(
                ObjectAnimator.ofFloat(card, "translationZ", 0f, -card.elevation)
            )
            duration = 300
        }

        val flipIn = AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(card, "rotationY", -90f, 0f)).with(
                ObjectAnimator.ofFloat(card, "scaleX", 0.5f, 1f)
            ).with(
                ObjectAnimator.ofFloat(card, "scaleY", 0.5f, 1f)
            ).with(
                ObjectAnimator.ofFloat(card, "translationZ", -card.elevation, 0f)
            )
            duration = 300
        }

        val textChange = ValueAnimator.ofObject(
            { fraction, startValue, endValue -> if (fraction < 0.5f) startValue else endValue },
            text.text,
            otherSideText
        ).apply {
            duration = 200
        }

        val set = AnimatorSet()

//        set.playTogether(flipOut, flipIn, textChange)
        set.playSequentially(flipOut, textChange, flipIn)
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

    private fun onClick() {
        var currCard = batch[cardCount]
        if (isFront) {
            // show back
            flipCard(currCard.back)
            binding.studyCardText.text = currCard.back
            isFront = false
            binding.studyCard.isClickable = false
            binding.studyCardText.text = currCard.front
            binding.easyButton.visibility = View.VISIBLE
            binding.mediumButton.visibility = View.VISIBLE
            binding.hardButton.visibility = View.VISIBLE
            binding.easyButton.isClickable = true
            binding.mediumButton.isClickable = true
            binding.hardButton.isClickable = true
        } else {
            cardCount += 1
            if (cardCount == batch.size) {
                showDialog()
            } else {
                // show new front
                currCard = batch[cardCount]
                flipCard(currCard.front)
                binding.studyCard.isClickable = true
                binding.studyCardText.text = currCard.front
                binding.easyButton.visibility = View.INVISIBLE
                binding.mediumButton.visibility = View.INVISIBLE
                binding.hardButton.visibility = View.INVISIBLE
                binding.easyButton.isClickable = false
                binding.mediumButton.isClickable = false
                binding.hardButton.isClickable = false

                isFront = true
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}