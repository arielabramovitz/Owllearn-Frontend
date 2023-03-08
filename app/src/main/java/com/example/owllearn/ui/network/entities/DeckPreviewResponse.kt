package com.example.gallery.network.entities

import com.example.owllearn.data.model.DeckPreview
import com.squareup.moshi.Json

/**
 * Secondary data class for moshi. Holds just a string of the "regular" field of the "urls" section of the GET response
 */

data class DeckPreviewResponse (
    @Json (name="statusCode")
    val statusCode: Int,
    @Json (name="body")
    val body: List<DeckPreview>

)
