package com.example.owllearn.ui.dashboard.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.owllearn.ui.dashboard.data.provider.DeckPreviewProvider
import com.example.owllearn.ui.dashboard.data.model.DeckPreview

class DecksPreviewViewModel : ViewModel() {
     val provider = DeckPreviewProvider()
     val _decks = MutableLiveData<List<DeckPreview>>()
//    private val _isLoading = MutableStateFlow(true)
//    val loading = _isLoading.asStateFlow()
    val decks: LiveData<List<DeckPreview>> = _decks

    init {
        // this is where initial user data loading will happen (get deck names from db)
        reloadDecks()

    }

    fun reloadDecks() {
        _decks.value = provider.getDecks("Test")
    }


}