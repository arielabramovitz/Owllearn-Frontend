package com.example.owllearn.ui.dashboard.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owllearn.ui.dashboard.data.DeckPreviewProvider
import com.example.owllearn.ui.dashboard.data.model.DeckPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DecksPreviewViewModel : ViewModel() {
     val provider = DeckPreviewProvider()
     val _decks = MutableLiveData<List<DeckPreview>>()
//    private val _isLoading = MutableStateFlow(true)
//    val loading = _isLoading.asStateFlow()
    val decks: LiveData<List<DeckPreview>> = _decks

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // this is where initial user data loading will happen (get deck names from db)
            reloadDecks()

            println(_decks.value)
        }
    }

    fun reloadDecks() {
        _decks.postValue(provider.getDecks("Test"))
    }


}