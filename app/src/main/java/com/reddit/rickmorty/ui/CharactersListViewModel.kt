package com.reddit.rickmorty.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.reddit.rickmorty.CoroutineContextProvider
import com.reddit.rickmorty.domain.FetchCharactersUseCase
import com.reddit.rickmorty.model.CharactersPageDataSource
import com.reddit.rickmorty.model.dto.CharacterDto
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CharactersListViewModel(
    contextProvider: CoroutineContextProvider,
    private val charactersApi: FetchCharactersUseCase
) : ViewModel() {

    lateinit var dataSource: CharactersPageDataSource

    val status = MutableLiveData<CharacterListState>(CharacterListState.Idle)

    val charactersList: LiveData<PagedList<CharacterDto>> by lazy {
        dataSource = CharactersPageDataSource { page, callback -> loadList(page, callback) }
        val factory = object : DataSource.Factory<Int, CharacterDto>() {
            override fun create() = dataSource
        }
        LivePagedListBuilder<Int, CharacterDto>(factory, 20).build()
    }

    fun loadList(
        page: Int, callback: (List<CharacterDto>) -> Unit
    ) {
        viewModelScope.launch(handler) {
            status.postValue(CharacterListState.Loading)
            val results = charactersApi.getCharacters(page)
            callback(results)
            status.postValue(CharacterListState.Idle)
        }
    }

    fun retry() {
        dataSource.retry()
    }

    private val handler = contextProvider.IO + CoroutineExceptionHandler { _, t ->
        Log.e(this::javaClass.name, "Error", t)
        status.postValue(CharacterListState.Error(t))
    }
}