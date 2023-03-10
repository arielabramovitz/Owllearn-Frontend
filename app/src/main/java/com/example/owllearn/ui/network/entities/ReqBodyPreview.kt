package com.example.gallery.network.entities

import com.example.owllearn.data.model.Card
import com.example.owllearn.data.model.Deck
import com.example.owllearn.data.model.DeckPreview
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Secondary data class for moshi. Holds just a string of the "regular" field of the "urls" section of the GET response
 */
@JsonClass(generateAdapter = true)
data class ReqBodyPreview (
    @Json (name="deckPreview")
    val deckPreview: DeckPreview,
    @Json (name="cards")
    val cards: List<Card>?
)
