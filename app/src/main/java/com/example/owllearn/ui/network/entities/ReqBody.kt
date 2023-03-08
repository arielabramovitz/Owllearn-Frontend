package com.example.gallery.network.entities

import com.example.owllearn.data.model.Deck
import com.example.owllearn.data.model.DeckPreview
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONObject

/**
 * Secondary data class for moshi. Holds just a string of the "regular" field of the "urls" section of the GET response
 */
@JsonClass(generateAdapter = true)
data class ReqBody (
    @Json (name="deck")
    val statusCode: Deck,
    @Json (name="deckPreview")
    val body: DeckPreview
)
