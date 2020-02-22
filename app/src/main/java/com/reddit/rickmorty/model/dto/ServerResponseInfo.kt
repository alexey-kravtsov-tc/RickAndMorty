package com.reddit.rickmorty.model.dto

data class ServerResponseInfo(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String
)