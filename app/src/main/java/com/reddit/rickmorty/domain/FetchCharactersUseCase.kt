package com.reddit.rickmorty.domain

import com.reddit.rickmorty.CoroutineContextProvider
import com.reddit.rickmorty.model.CharactersApiInterface
import com.reddit.rickmorty.model.dto.CharacterDto
import kotlinx.coroutines.withContext

class FetchCharactersUseCase(
    private val provider: CoroutineContextProvider,
    private val api: CharactersApiInterface
) {

    //TODO: to apply offline storage, database as a single source of truth should be placed here
    //      therefore UI will receive updates from DB only and all data from network goes to DB
    suspend operator fun invoke(page: Int): List<CharacterDto> = withContext(provider.IO) {
        api.getAllCharacters(page).results
    }
}