package com.example.gallery.network.entities

import com.example.owllearn.data.model.Deck
import com.example.owllearn.data.model.DeckPreview
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Secondary data class for moshi. Holds just a string of the "regular" field of the "urls" section of the GET response
 */
@JsonClass(generateAdapter = true)
data class ReqBodyDeck (
    @Json (name="deck")
    val deck: Deck,
    @Json (name="deckPreview")
    val deckPreview: DeckPreview
)
