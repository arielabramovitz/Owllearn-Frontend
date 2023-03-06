package com.example.owllearn.ui.dashboard.data.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owllearn.databinding.FragmentDashboardBinding
import com.example.owllearn.ui.dashboard.data.provider.DeckPreviewProvider
import com.example.owllearn.ui.dashboard.data.model.DeckPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*

class DecksPreviewViewModel(private val userId: String) : ViewModel() {
    val provider = DeckPreviewProvider()
    val _decks = MutableLiveData<List<DeckPreview>>()
    val decks: LiveData<List<DeckPreview>> = _decks

    init {
        // this is where initial user data loading will happen (get deck names from db)
        reloadDeckPreviews()

    }

    fun reloadDeckPreviews() {

        viewModelScope.launch(Dispatchers.IO) {
            _decks.postValue(provider.getDeckPreviews(userId))

        }

    }


}