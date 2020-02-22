package com.reddit.rickmorty.model.dto

data class ServerChatacterListResponse(
    val info: ServerResponseInfo,
    val results: List<CharacterDto>
)
