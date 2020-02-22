package com.reddit.rickmorty.model

import com.reddit.rickmorty.model.dto.ServerChatacterListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersApiInterface {

    @GET("character")
    suspend fun getAllCharacters(@Query("page") page: Int? = null): ServerChatacterListResponse

}