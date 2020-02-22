package com.reddit.rickmorty.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reddit.rickmorty.R
import com.reddit.rickmorty.databinding.ItemCharacterBinding
import com.reddit.rickmorty.model.dto.CharacterDto

class CharactersAdapter :
    PagedListAdapter<CharacterDto, CharactersAdapter.CharacterViewHolder>(CharacterDiff()) {

    class CharacterDiff : DiffUtil.ItemCallback<CharacterDto>() {
        override fun areItemsTheSame(oldItem: CharacterDto, newItem: CharacterDto): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: CharacterDto, newItem: CharacterDto): Boolean =
            newItem.name == oldItem.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.binding?.character = getItem(position)
    }

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemCharacterBinding>(view)
    }
}