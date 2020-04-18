package com.reddit.rickmorty.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.reddit.rickmorty.R
import kotlinx.android.synthetic.main.fragment_characters_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException

class CharactersListFragment : Fragment() {

    private val vm: CharactersListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CharactersAdapter{ character ->
            val action = CharactersListFragmentDirections.actionOpenDetails(character)
            Navigation.findNavController(view).navigate(action)
        }
        characterList.adapter = adapter

        vm.charactersList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        retry.setOnClickListener {
            vm.retry()
        }

        vm.status.observe(viewLifecycleOwner, Observer {
            placeholder?.isVisible = it !is CharacterListState.Data
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
