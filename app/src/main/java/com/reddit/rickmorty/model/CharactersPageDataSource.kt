package com.reddit.rickmorty.model

import androidx.paging.PageKeyedDataSource
import com.reddit.rickmorty.model.dto.CharacterDto

class CharactersPageDataSource(
    //TODO: pass UseCase, coroutine scope and context and perform action in this class
    val loadPage : (Int, (List<CharacterDto>) -> Unit) -> Unit
) : PageKeyedDataSource<Int, CharacterDto>() {

    private var retryQuery: (() -> Any)? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, CharacterDto>
    ) {
        retryQuery = { loadInitial(params, callback) }
        loadPage(1) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CharacterDto>) {
        val page = params.key
        retryQuery = { loadAfter(params, callback) }
        loadPage(page) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CharacterDto>) {
        val page = params.key
        retryQuery = { loadBefore(params, callback) }
        loadPage(page) {
            callback.onResult(it, page - 1)
        }
    }

    fun retry() {
        retryQuery?.invoke()
    }
}