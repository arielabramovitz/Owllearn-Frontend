package com.example.owllearn.ui.decks.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.owllearn.ui.decks.data.model.Deck
import com.example.owllearn.ui.decks.data.provider.DecksProvider

class DecksViewModel(private val provider: DecksProvider, private val uid: String) : ViewModel() {

    private val _decks = MutableLiveData<MutableList<Deck>>()
    val decks: LiveData<MutableList<Deck>> = _decks
    val decksMap: MutableMap<String, Deck> = mutableMapOf()

    init {
        reloadDecks()
    }

    fun addDeck(deck: Deck) {
        provider.addDeck(deck)
    }

    fun reloadDecks() {
        val currDecks = provider.getDecks(uid)
        _decks.postValue(currDecks)
        decksMap.clear()
        currDecks.forEach {
            decksMap[it.deckId] = it
        }

    }
}