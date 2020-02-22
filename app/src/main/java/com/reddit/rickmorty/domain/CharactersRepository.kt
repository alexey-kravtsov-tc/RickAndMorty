package com.reddit.rickmorty.domain

import com.reddit.rickmorty.model.CharactersApiInterface

class CharactersRepository(
    private val api: CharactersApiInterface
) {

    suspend fun getCharacters(page: Int) = api.getAllCharacters(page)
}