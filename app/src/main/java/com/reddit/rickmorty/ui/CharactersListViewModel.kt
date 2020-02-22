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
import com.reddit.rickmorty.model.CharactersPageDataSource
import com.reddit.rickmorty.model.dto.CharacterDto
import com.reddit.rickmorty.domain.CharactersRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CharactersListViewModel(
    contextProvider: CoroutineContextProvider,
    private val charactersApi: CharactersRepository
) : ViewModel() {

    lateinit var dataSource: CharactersPageDataSource

    val status = MutableLiveData<CharacterListState>(CharacterListState.Idle)

    val charactersList: LiveData<PagedList<CharacterDto>> by lazy {

        dataSource =
            CharactersPageDataSource { page, callback ->
                viewModelScope.launch(handler) {
                    status.postValue(CharacterListState.Loading)
                    val response = charactersApi.getCharacters(page)
                    val results = response.results
                    callback(results)
                    status.postValue(CharacterListState.Idle)
                }
            }
        val factory = object : DataSource.Factory<Int, CharacterDto>() {
            override fun create() = dataSource
        }
        LivePagedListBuilder<Int, CharacterDto>(factory, 20).build()
    }

    fun retry() {
        dataSource.retry()
    }

    private val handler = contextProvider.IO + CoroutineExceptionHandler { _, t ->
        Log.e(this::javaClass.name, "Error", t)
        status.postValue(CharacterListState.Error(t))
    }
}