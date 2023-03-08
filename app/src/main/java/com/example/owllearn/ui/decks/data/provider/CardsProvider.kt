package com.example.owllearn.ui.decks.data.provider

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.model.Deck
import com.example.owllearn.ui.network.OwllearnApi
import com.squareup.moshi.Moshi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import kotlin.coroutines.coroutineContext

class CardsProvider(private val userId: String?, private val context: Context) {
    private val api = OwllearnApi.instance

    suspend fun getCards(deckId: String): Deck? {
        val response = api.getSingleDeck(userId!!, deckId)

        if (response.isSuccessful && response.body() != null) {

            return response.body()!!

        }
        return null
    }

    suspend fun createCard(card: Card): Card? {

        val response = api.createCard(userId!!, card)
        return response.body()

    }

    suspend fun editCard(card: Card): Card? {
        Log.d("HEEEEERE", userId.toString())
        val response = api.editCard(userId!!, card)
        return response.body()
    }

    suspend fun deleteCard(card: Card) {
        val response = api.deleteCard(deckId = card.deckId, cardId = card.cardId, userId = userId!!)
    }

}