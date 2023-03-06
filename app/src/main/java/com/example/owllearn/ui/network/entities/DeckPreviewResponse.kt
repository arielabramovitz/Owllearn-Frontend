package com.example.gallery.network.entities

import com.example.owllearn.ui.dashboard.data.model.DeckPreview
import com.example.owllearn.ui.decks.data.model.Card
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONObject

/**
 * Secondary data class for moshi. Holds just a string of the "regular" field of the "urls" section of the GET response
 */

data class DeckPreviewResponse (
    @Json (name="statusCode")
    val statusCode: Int,
    @Json (name="body")
    val body: List<DeckPreview>

)
