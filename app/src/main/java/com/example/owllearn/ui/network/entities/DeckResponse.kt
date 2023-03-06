package com.example.gallery.network.entities

import com.example.owllearn.ui.decks.data.model.Card
import com.example.owllearn.ui.decks.data.model.Deck
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONObject

/**
 * Secondary data class for moshi. Holds just a string of the "regular" field of the "urls" section of the GET response
 */
@JsonClass(generateAdapter = true)
data class DeckResponse (
    @Json (name="statusCode")
    val statusCode: Int,
    @Json (name="body")
    val body: JSONObject
)
