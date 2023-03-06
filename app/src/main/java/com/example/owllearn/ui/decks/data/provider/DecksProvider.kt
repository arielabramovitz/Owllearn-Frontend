package com.example.owllearn.ui.decks.data.provider

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.owllearn.ui.decks.data.model.Card
import com.example.owllearn.ui.decks.data.model.Deck
import com.example.owllearn.ui.network.OwllearnApi
import com.squareup.moshi.Moshi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import kotlin.coroutines.coroutineContext

class DecksProvider(private val userId: String, private val context: Context) {
    private val api = OwllearnApi.instance

    // mock, replace with database query
    suspend fun getDecks(uid: String): List<Deck> {
        val response = api.getAllDecks(uid)

        if (response.isSuccessful && response.body() != null) {

            return response.body()!!

        }
        return mutableListOf()
    }

    suspend fun createDeck(deck: Deck) {
        val deckAsJson = OwllearnApi.deckAdapter.toJson(deck)
        val request = deckAsJson.toRequestBody("application/json".toMediaType())
        val response = api.createDeck(request)
        if (!response.isSuccessful) {
            Toast.makeText(context, "Something went wrong creating a deck..", Toast.LENGTH_SHORT).show()

        }
    }

    companion object {
        val decks = mutableListOf<Deck>(

        )
//        init {
//            for (i in 0..4) {
//                val cards = mutableListOf<Card>()
//                for (j in 0..4) {
//                    cards.add(
//                        Card(
//                            UUID.randomUUID().toString(),
//                            "front of card ${j * i + i}",
//                            "back of card ${j * i + i}"
//                        )
//                    )
//                }
//
//                val deck = Deck("Deck $i" ,UUID.randomUUID().toString(), cards)
//                decks.add(deck)
//            }
//        }

    }
}