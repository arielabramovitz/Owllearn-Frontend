package com.example.gallery.network.entities

import com.example.owllearn.ui.decks.data.model.Card
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Secondary data class for moshi. Holds just a string of the "regular" field of the "urls" section of the GET response
 */
@JsonClass(generateAdapter = true)
data class ReqResponse (

    @Json (name="statusCode")
    val statusCode: Int
)
