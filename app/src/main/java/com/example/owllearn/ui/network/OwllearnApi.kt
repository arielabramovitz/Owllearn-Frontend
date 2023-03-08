package com.example.owllearn.ui.network
import com.example.gallery.network.entities.ReqBody
import com.example.owllearn.data.model.DeckPreview
import com.example.owllearn.data.model.Card
import com.example.owllearn.data.model.Deck
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

/**
 * API for communicating with the REST API on AWS
 * Some of this API was made with the intention of allowing this
 * app to be improved later on, and won't be used in the submission.
 */
internal interface OwllearnApi {

    /**
     * sends a GET request for all decks belonging to a userS
     */
    @GET("decks")
    suspend fun getAllDecks(
        @Query("userId") userId: String
    ): Response<List<Deck>>

    @POST("decks")
    suspend fun createDeck(
        @Body body: Deck
    ): Response<Deck>

    @DELETE("decks")
    suspend fun deleteDeck(
        @Query("userId") userId: String,
        @Query("deckId") deckId: String
    ): ResponseBody

    @PUT("decks")
    suspend fun editDeck(
        @Body body: ReqBody
    ): Response<Deck>

    /**
     * sends a GET request for all cards belonging to a user
     */
    @GET("cards")
    suspend fun getAllCards(
        @Query("userId") userId: String
    ): Response<List<Card>>

    /**
     * sends a GET request for a single card
     */
    @GET("cards")
    suspend fun getSingleCard(
        @Query("userId") userId: String,
        @Query("deckId") deckId: String,
        @Query("cardId") cardId: String
    ): Response<Card>

    @POST("cards")
    suspend fun createCard(
        @Query("userId") userId: String,
        @Body body: Card
    ): Response<Card>

    @DELETE("cards")
    suspend fun deleteCard(
        @Query("userId") userId: String,
        @Query("deckId") deckId: String,
        @Query("cardId") cardId: String
    ): ResponseBody

    @PUT("cards")
    suspend fun editCard(
        @Query("userId") userId: String,
        @Body body: Card
    ): Response<Card>

    /**
     * sends a GET request for all deck previews belonging to a user
     */
    @GET("deck-previews")
    suspend fun getAllDeckPreviews(
        @Query("userId") userId: String
    ): Response<List<DeckPreview>>

    @GET("deck-previews")
    suspend fun getSingleDeckPreviews(
        @Query("userId") userId: String
    ): Response<DeckPreview>

    @POST("deck-previews")
    suspend fun createDeckPreview(
        @Body body: DeckPreview
    ): Response<DeckPreview>

    @DELETE("deck-previews")
    suspend fun deleteCDeckPreview(
        @Query("deckId") deckId: String,
        @Query("cardId") cardId: String
    ): ResponseBody

    @PUT("deck-previews")
    suspend fun editDeckPreview(
        @Body body: DeckPreview
    ): Response<DeckPreview>

    /**
     * companions needed for the API
     */
    companion object {
        private const val BASE_URL = "https://77x0u4uueb.execute-api.us-east-1.amazonaws.com/prod/"
        private lateinit var moshi: Moshi


        val instance: OwllearnApi by lazy {
            val retrofit: Retrofit = createRetrofit()
            retrofit.create(OwllearnApi::class.java)
        }

        private fun createRetrofit(): Retrofit {

            // Converter
            moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

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
