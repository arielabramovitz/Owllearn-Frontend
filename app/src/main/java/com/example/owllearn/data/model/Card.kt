package com.example.owllearn.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Card(
    val cardId: String,
    val deckId: String,
    var front: String,
    var back: String,
    var ranking: String = "unmarked"
) : Parcelable
