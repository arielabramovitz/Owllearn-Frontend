package com.example.owllearn.ui.decks.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Card(
    val cardId: String,
    val deckId: String,
    val front: String,
    val back: String,
) : Parcelable
