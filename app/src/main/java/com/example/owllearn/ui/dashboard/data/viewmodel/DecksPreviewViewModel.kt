package com.example.owllearn.ui.dashboard.data.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.owllearn.databinding.FragmentDashboardBinding
import com.example.owllearn.ui.dashboard.data.provider.DeckPreviewProvider
import com.example.owllearn.ui.dashboard.data.model.DeckPreview
import kotlinx.coroutines.flow.combine
import java.util.*

class DecksPreviewViewModel (private val application: Application?) : ViewModel() {
     val provider = DeckPreviewProvider()
     val _decks = MutableLiveData<List<DeckPreview>>()
    val decks: LiveData<List<DeckPreview>> = _decks

    init {
        // this is where initial user data loading will happen (get deck names from db)
        reloadDeckPreviews()

    }

    fun reloadDeckPreviews() {
        val uuid = application?.getSharedPreferences(
            "PREFERENCE", AppCompatActivity.MODE_PRIVATE)
            ?.getString("user_id", null)


        if (uuid != null) {
            _decks.value = provider.getDecks(UUID.randomUUID().toString())
        }
    }


}