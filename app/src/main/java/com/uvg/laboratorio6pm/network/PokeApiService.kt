package com.uvg.laboratorio6pm.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class Pokemon(val name: String, val url: String)

data class PokemonSprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    val back_shiny: String?
)

data class PokemonStat(
    val base_stat: Int,
    val stat: Stat
)

data class Stat(
    val name: String
)

data class PokemonType(val type: Type)
data class Type(val name: String)

data class PokemonSpecies(
    val types: List<PokemonType>
)

data class Species(val name: String)

data class PokemonDetail(
    val sprites: PokemonSprites,
    val stats: List<PokemonStat>?,
    val types: List<PokemonType>?
)

data class PokeResponse(val results: List<Pokemon>)

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int): PokeResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDetail

    @GET("pokemon-species/{name}")
    suspend fun getPokemonSpecies(@Path("name") name: String): PokemonSpecies
}

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: PokeApiService = retrofit.create(PokeApiService::class.java)
}