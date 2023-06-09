package com.example.owllearn.ui.study

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
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
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.model.Deck
import com.example.owllearn.data.model.DeckPreview
import com.example.owllearn.data.viewmodel.SharedViewModel
import com.example.owllearn.databinding.FragmentStudyBatchBinding
import com.example.owllearn.util.consts

class StudyBatchFragment : Fragment() {

    private var _binding: FragmentStudyBatchBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var cardCount: Int = 0
    private var isFront = true
    private lateinit var batch: List<Card>
    private lateinit var deckPreview: DeckPreview
    private lateinit var deck: Deck
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val preferences = requireActivity().getSharedPreferences("PREFERENCE", AppCompatActivity.MODE_PRIVATE)
        _binding = FragmentStudyBatchBinding.inflate(inflater, container, false)
        val userId = preferences.getString(consts.UID, null)
        deck = sharedViewModel.currDeck.value!!
        deckPreview = sharedViewModel.getPreview(deck.deckId)!!
        val deckName = sharedViewModel.currDeck.value?.deckName
        binding.studyBatchText.text = String.format(binding.studyBatchText.text.toString(), deckName)
        val batchSize = arguments?.getInt("batchSize", 0)
        batch = getBatch(batchSize!!)!!
        val firstCard = batch[0]
        binding.studyCardText.text = firstCard.front
        binding.studyCard.setOnClickListener {
            onClick("")
        }
        binding.easyButton.setOnClickListener {
            onClick("easy")
        }
        binding.mediumButton.setOnClickListener {
            onClick("medium")
        }
        binding.hardButton.setOnClickListener {
            onClick("hard")
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

        set.playSequentially(flipOut, textChange, flipIn)
        set.start()

    }

    private fun moveToStudy() {
        val action = StudyBatchFragmentDirections.actionStudyBatchToStudy()
        findNavController().navigate(action)
    }

    private fun changeRank(currCard: Card, newRank: String) {
        val oldRank = currCard.ranking
        currCard.ranking = newRank
        when (oldRank) {
            "unmarked"->deckPreview.unmarked-=1
            "easy"->deckPreview.easy-=1
            "medium"->deckPreview.medium-=1
            "hard"->deckPreview.hard-=1
        }
        when(newRank) {
            "easy"->deckPreview.easy+=1
            "medium"->deckPreview.medium+=1
            "hard"->deckPreview.hard+=1

        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage("The session is over")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                sharedViewModel.uploadPreview(
                    sharedViewModel.currDeck.value!!.deckId,
                    batch
                )
                moveToStudy()

            }

        val alert = builder.create()
        alert.show()
    }

    private fun onClick(newRank: String) {
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
            changeRank(currCard, newRank)
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