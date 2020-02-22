package com.reddit.rickmorty.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.reddit.rickmorty.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException

class CharactersListActivity : AppCompatActivity() {

    private val vm: CharactersListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = CharactersAdapter()
        characterList.adapter = adapter

        vm.charactersList.observe(this, Observer {
            adapter.submitList(it)
        })

        retry.setOnClickListener {
            vm.retry()
        }

        vm.status.observe(this, Observer {
            placeholder?.isVisible = it !is CharacterListState.Idle
            retry?.isVisible = it is CharacterListState.Error
            placeholder.text =
                when (it) {
                    is CharacterListState.Error -> getErrorMessage(it.throwable)
                    is CharacterListState.Loading -> getString(R.string.loading)
                    else -> ""
                }
        })
    }

    private fun getErrorMessage(throwable: Throwable) = getString(
        when (throwable) {
            is IOException -> R.string.network_error
            else -> R.string.generic_error
        }
    )
}
