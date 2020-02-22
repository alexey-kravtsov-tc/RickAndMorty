package com.reddit.rickmorty.ui

sealed class CharacterListState {
    object Idle : CharacterListState()
    object Loading : CharacterListState()
    data class Error(val throwable: Throwable) : CharacterListState()
}