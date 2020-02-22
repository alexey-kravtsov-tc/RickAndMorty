package com.reddit.rickmorty.model.dto

data class ServerCharacterListResponse(
    val info: ServerResponseInfo,
    val results: List<CharacterDto>
)
