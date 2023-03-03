package com.example.owllearn.ui.decks.data.provider

import com.example.owllearn.ui.decks.data.model.Card
import com.example.owllearn.ui.decks.data.model.Deck
import java.util.*

class DecksProvider {


    // mock, replace with database query
    fun getDecks(uid: String): MutableList<Deck> {
        return decks
    }

    fun addDeck(deck: Deck) {
        decks.add(deck)
    }

    companion object {
        val decks = mutableListOf<Deck>(

        )
        init {
            for (i in 0..4) {
                val cards = mutableListOf<Card>()
                for (j in 0..4) {
                    cards.add(
                        Card(
                            UUID.randomUUID().toString(),
                            "front of card ${j * i + i}",
                            "back of card ${j * i + i}"
                        )
                    )
                }

                val deck = Deck("Deck $i" ,UUID.randomUUID().toString(), cards)
                decks.add(deck)
            }
        }

    }
}