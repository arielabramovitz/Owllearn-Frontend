package com.example.owllearn.ui.decks.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.owllearn.ui.decks.data.model.Card
import com.example.owllearn.ui.decks.data.model.Deck
import com.example.owllearn.ui.decks.data.provider.DecksProvider

class CardsViewModel(private val deckViewModel: DecksViewModel, private val deckId: String) : ViewModel() {

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    init {
        reloadCards()
    }

    fun reloadCards() {
        val cards = deckViewModel.decksMap[deckId]?.cards
        _cards.postValue(cards!!)
    }
}