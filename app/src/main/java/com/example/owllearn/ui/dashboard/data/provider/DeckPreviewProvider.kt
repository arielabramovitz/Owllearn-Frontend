package com.example.owllearn.ui.dashboard.data.provider

import android.util.Log
import com.example.owllearn.ui.dashboard.data.model.DeckPreview
import com.example.owllearn.ui.network.OwllearnApi
import okhttp3.internal.notify
import java.util.*

class DeckPreviewProvider {
    private val api = OwllearnApi.instance
    suspend fun getDeckPreviews(userId: String): List<DeckPreview> {
        val response = api.getAllDeckPreviews(userId)
        if (response.isSuccessful && response.body() != null) {
            Log.d("LOG", response.body().toString())
            return response.body()!!
        }
        return emptyList()

    }
}