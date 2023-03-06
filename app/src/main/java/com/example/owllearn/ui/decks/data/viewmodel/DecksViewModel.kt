package com.example.owllearn.ui.decks.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owllearn.ui.decks.data.model.Deck
import com.example.owllearn.ui.decks.data.provider.DecksProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DecksViewModel(private val provider: DecksProvider, private val uid: String) : ViewModel() {

    private val _decks = MutableLiveData<List<Deck>>()
    val decks: LiveData<List<Deck>> = _decks
    val decksMap: MutableMap<String, Deck> = mutableMapOf()

    init {
        reloadDecks()
    }

    fun createDeck(deckName: String) {
        viewModelScope.launch (Dispatchers.IO){
            provider.createDeck(Deck(deckName, UUID.randomUUID().toString(), uid, emptyList()))

        }
    }

    fun reloadDecks() {
        viewModelScope.launch (Dispatchers.IO){

            val currDecks = provider.getDecks(uid)
            _decks.postValue(currDecks)
            decksMap.clear()
            currDecks.forEach {
                decksMap[it.deckId] = it
            }
        }

    }
}