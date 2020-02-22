package com.reddit.rickmorty.domain

import com.reddit.rickmorty.model.CharactersApiInterface
import com.reddit.rickmorty.model.dto.CharacterDto

class FetchCharactersUseCase(
    private val api: CharactersApiInterface
) {

    //TODO: to apply offline storage, database as a single source of truth should be placed here
    //      therefore UI will receive updates from DB only and all data from network goes to DB
    suspend fun getCharacters(page: Int): List<CharacterDto> {
        val response = api.getAllCharacters(page)
        return response.results
    }
}