package com.reddit.rickmorty.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.reddit.rickmorty.databinding.FragmentCharactersDetailsBinding
import com.reddit.rickmorty.model.dto.CharacterDto
import com.reddit.rickmorty.ui.CharacterDetailsFragmentArgs
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CharacterDetailsFragment: Fragment() {

    private val args: CharacterDetailsFragmentArgs by navArgs()

    private val vm: CharacterDetailsViewModel by viewModel { parametersOf(args.character)  }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentCharactersDetailsBinding.inflate(inflater, container, false).also {
        it.vm = vm
        it.lifecycleOwner = this
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}

class CharacterDetailsViewModel(val character: CharacterDto): ViewModel() {


}