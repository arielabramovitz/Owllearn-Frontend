package com.example.owllearn

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.model.Deck
import com.example.owllearn.data.model.DeckPreview
import com.example.owllearn.data.viewmodel.SharedViewModel
import com.example.owllearn.ui.network.SharedProvider
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ViewModelUnitTests {

    @get:Rule
    val instantLiveData = InstantTaskExecutorRule()

    @Mock
    val fakeProvider: SharedProvider = Mockito.mock(SharedProvider::class.java)

    @Test
    fun onCreateViewModel_DecksAndPreviewsAreNull() = runTest {
        Mockito.`when`(fakeProvider.getDecks(mockUserId)).thenReturn(emptyList())
        Mockito.`when`(fakeProvider.getDeckPreviews(mockUserId)).thenReturn(emptyList())

        val viewModel = SharedViewModel(provider = fakeProvider, dispatcher = UnconfinedTestDispatcher())
        Truth.assertThat(viewModel.previews.value).isNull()
        Truth.assertThat(viewModel.decks.value).isNull()
    }

    @Test
    fun onLoadDecksAndPreviews_DecksAndPreviewsAreLoaded() = runTest {
        Mockito.`when`(fakeProvider.getDecks(mockUserId)).thenReturn(mutableListOf(mockDeck))
        Mockito.`when`(fakeProvider.getDeckPreviews(mockUserId)).thenReturn(mutableListOf(mockPreview))

        val viewModel = SharedViewModel(provider = fakeProvider, dispatcher = UnconfinedTestDispatcher())
        viewModel.reloadDecks(mockUserId)
        viewModel.reloadDeckPreviews(mockUserId)
        Truth.assertThat(viewModel.previews.value).isNotNull()
        Truth.assertThat(viewModel.decks.value).isNotNull()
        Truth.assertThat(viewModel.decks.value?.get(0)?.cards?.get(0)?.cardId).isEqualTo(mockCardA.cardId)
        Truth.assertThat(viewModel.decks.value?.get(0)?.cards?.get(1)?.cardId).isEqualTo(mockCardB.cardId)
    }

    companion object {
        val mockUserId = "mock-user"
        val mockDeckId = "mock-deck"
        val mockDeckName = mockDeckId
        val mockCardA = Card("card-id-a", mockDeckId, "front-text-a", "back-text-a")
        val mockCardB = Card("card-id-b", mockDeckId, "front-text-b", "back-text-b")
        val mockDeck = Deck(mockDeckName, mockDeckId, mockUserId, mutableListOf(mockCardA, mockCardB))
        val mockPreview = DeckPreview(
            userId = mockUserId, deckId = mockDeckId,
            deckName = mockDeckName, unmarked = 2, easy = 0, medium = 0, hard = 0
        )
    }

}