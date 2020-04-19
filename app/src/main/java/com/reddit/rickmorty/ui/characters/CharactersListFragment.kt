package com.reddit.rickmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.transition.TransitionInflater
import com.reddit.rickmorty.R
import com.reddit.rickmorty.ui.details.CharactersAdapter
import kotlinx.android.synthetic.main.fragment_characters_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class CharactersListFragment : Fragment() {

    private val vm: CharactersListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CharactersAdapter { character, extras ->
            val action = CharactersListFragmentDirections.actionOpenDetails(character)
            Navigation.findNavController(view).let {
                if (it.currentDestination?.id == R.id.charactersListFragment)
                    it.navigate(action, extras)
            }
        }

        characterList.apply {
            this.adapter = adapter
            //workaround for back animation
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }

        vm.charactersList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        vm.status.observe(viewLifecycleOwner, Observer {
            adapter.setState(it)
        })
    }
}
