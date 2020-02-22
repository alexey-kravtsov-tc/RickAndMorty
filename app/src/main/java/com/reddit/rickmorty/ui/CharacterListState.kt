package com.reddit.rickmorty.ui

sealed class CharacterListState {
    object Data : CharacterListState()
    object Loading : CharacterListState()
    data class Error(val throwable: Throwable) : CharacterListState()
}