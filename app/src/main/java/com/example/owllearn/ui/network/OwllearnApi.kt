package com.example.owllearn.ui.network

import com.example.gallery.DeckAdapter
import com.example.gallery.DeckPreviewAdapter
import com.example.gallery.network.entities.CardResponse
import com.example.gallery.network.entities.DeckPreviewResponse
import com.example.gallery.network.entities.DeckResponse
import com.example.gallery.network.entities.ReqResponse
import com.example.owllearn.ui.dashboard.data.model.DeckPreview
import com.example.owllearn.ui.decks.data.model.Card
import com.example.owllearn.ui.decks.data.model.Deck
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

internal interface OwllearnApi {

    /**
     * sends a GET request for all decks belonging to a userS
     */
    @GET("decks")
    suspend fun getAllDecks(
        @Query("userId") userId: String
    ): Response<List<Deck>>

    /**
     * sends a GET request for a single deck
     */
    @GET("decks")
    suspend fun getSingleDeck(
        @Query("userId") userId: String,
        @Query("deckId") deckId: String
    ): Response<DeckResponse>

    /**
     * sends a GET request for all cards belonging to a user
     */
    @GET("cards")
    suspend fun getAllCards(
        @Query("userId") userId: String
    ): Response<List<CardResponse>>

    /**
     * sends a GET request for a single card
     */
    @GET("cards")
    suspend fun getSingleCard(
        @Query("deckId") userId: String,
        @Query("cardId") deckId: String
    ): Response<CardResponse>

    /**
     * sends a GET request for all deck previews belonging to a user
     */
    @GET("deck-previews")
    suspend fun getAllDeckPreviews(
        @Query("userId") userId: String
    ): Response<List<DeckPreview>>

    @POST("decks")
    suspend fun createDeck(
        @Body body: RequestBody
    ): Response<ReqResponse>

    @POST("cards")
    suspend fun createCard(
        @Body body: JSONObject
    ): Response<ReqResponse>

    @DELETE("decks")
    suspend fun deleteDeck(
        @Body body: JSONObject
    ): Response<ReqResponse>

    @DELETE("cards")
    suspend fun deleteCard(
        @Body body: JSONObject
    ): Response<ReqResponse>

    @PUT("decks")
    suspend fun editDeck(
        @Body body: JSONObject
    ): Response<ReqResponse>

    @PUT("cards")
    suspend fun editCard(
        @Body body: JSONObject
    ): Response<ReqResponse>

    /**
     * companions needed for the API
     */
    companion object {
        private const val BASE_URL = "https://77x0u4uueb.execute-api.us-east-1.amazonaws.com/prod/"
        private lateinit var moshi: Moshi
        lateinit var deckAdapter: JsonAdapter<Deck>
        lateinit var deckPreviewAdapter: JsonAdapter<DeckPreview>
        lateinit var cardAdapter: JsonAdapter<Card>

        val instance: OwllearnApi by lazy {
            val retrofit: Retrofit = createRetrofit()
            retrofit.create(OwllearnApi::class.java)
        }

        private fun createRetrofit(): Retrofit {

            // Converter
            moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            deckAdapter = moshi.adapter(Deck::class.java)
            deckPreviewAdapter = moshi.adapter(DeckPreview::class.java)
            cardAdapter = moshi.adapter(Card::class.java)
            val t = Types.newParameterizedType(List::class.java, DeckPreview::class.java)
            // Logger
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            // HTTP Client with two interceptors: one to inject header with authkey and one to inject logger
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val req = chain.request().newBuilder()
                        .build()
                    chain.proceed(req)
                })
                .addInterceptor(logging)
                .build()

            // Retrofit
            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build()
        }
    }

}
