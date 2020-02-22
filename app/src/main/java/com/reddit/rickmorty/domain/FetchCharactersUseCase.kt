package com.reddit.rickmorty.domain

import com.reddit.rickmorty.model.CharactersApiInterface
import com.reddit.rickmorty.model.dto.CharacterDto

class FetchCharactersUseCase(
    private val api: CharactersApiInterface
) {

    suspend fun getCharacters(page: Int): List<CharacterDto> {
        val response = api.getAllCharacters(page)
        return response.results
    }
}