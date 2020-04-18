package com.reddit.rickmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import com.reddit.rickmorty.R
import com.reddit.rickmorty.model.dto.CharacterDto
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CharacterDetailsFragment: Fragment() {

    private val args: CharacterDetailsFragmentArgs by navArgs()

    private val vm: CharacterDetailsViewModel by viewModel { parametersOf(args.character)  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}

class CharacterDetailsViewModel(val character: CharacterDto): ViewModel() {


}