package com.example.owllearn.ui.decks.data.provider

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.gallery.network.entities.ReqBody
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.model.Deck
import com.example.owllearn.data.model.DeckPreview
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

    /**
     * Currently implemented in this way:
     * after save button has been pressed, the cards in the view model are saved into the local deck and then the deck
     * stored in the DB is overwritten with the local deck using the same deck ID but with the new cards
     */
    suspend fun editDeck(deck: Deck, deckPreview: DeckPreview): Deck? {
        val response = api.editDeck(ReqBody(deck, deckPreview))
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        }
        return null
    }

    suspend fun createDeck(deck: Deck): Deck? {

        val response = api.createDeck(deck)
        return response.body()!!
        // TODO: Figure out how to create a toast if the deck wasnt created - currently collapses because
        //  this call is using Dispatcher.IO

//        if (!response.isSuccessful) {
//            Toast.makeText(context, "Something went wrong creating a deck..", Toast.LENGTH_SHORT).show()
//
//        }
    }

    suspend fun deleteDeck(deck: Deck) {
        val response = api.deleteDeck(deckId = deck.deckId, userId = deck.userId)
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