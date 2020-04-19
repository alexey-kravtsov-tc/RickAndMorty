package com.reddit.rickmorty.ui.details

import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reddit.rickmorty.R
import com.reddit.rickmorty.databinding.ItemCharacterBinding
import com.reddit.rickmorty.model.dto.CharacterDto
import com.reddit.rickmorty.ui.characters.CharacterListState
import kotlinx.android.synthetic.main.item_character.view.*
import kotlinx.android.synthetic.main.item_footer.view.*
import java.io.IOException

class CharactersAdapter(private val onCharacterClick: (CharacterDto, Navigator.Extras) -> Unit) :
    PagedListAdapter<CharacterDto, RecyclerView.ViewHolder>(CharacterDiff()) {

    companion object {
        private const val DATA_VIEW_TYPE = 1
        private const val FOOTER_VIEW_TYPE = 2
    }

    private var state: CharacterListState = CharacterListState.Loading

    class CharacterDiff : DiffUtil.ItemCallback<CharacterDto>() {
        override fun areItemsTheSame(oldItem: CharacterDto, newItem: CharacterDto): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: CharacterDto, newItem: CharacterDto): Boolean =
            newItem.name == oldItem.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == DATA_VIEW_TYPE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_character, parent, false)
            return CharacterViewHolder(view, onCharacterClick)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_footer, parent, false)
            return FooterViewHolder(view, {})
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CharacterViewHolder) {
            holder.itemView.avatar.transitionName = "transition${getItem(position)?.id}"
            holder.binding?.character = getItem(position)
        } else if (holder is FooterViewHolder) {
            holder.bind(state)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 0 else 1
    }

    fun hasFooter() = state is CharacterListState.Data

    fun setState(state: CharacterListState) {
        this.state = state
        notifyItemChanged(super.getItemCount()) //??
    }

    class CharacterViewHolder(
        view: View,
        onClick: (CharacterDto, Navigator.Extras) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        val binding = DataBindingUtil.bind<ItemCharacterBinding>(view)

        //TODO: could be databind with interface
        init {
            view.setOnClickListener {
                val extras = FragmentNavigator.Extras.Builder()
                extras.addSharedElement(view.avatar, "item_transformation")
                binding?.character?.let { onClick(it, extras.build()) }
            }
        }
    }

    class FooterViewHolder(
        val view: View,
        // TODO: Retry logic
        onRetry: () -> Unit
    ) : RecyclerView.ViewHolder(view) {


        fun bind(state: CharacterListState) {
            when (state) {
                CharacterListState.Loading -> {
                    (view.loadingView.foreground as AnimationDrawable).let {
                        it.start()
                    }
                    view.loadingView.isVisible = true
                }

                CharacterListState.Data -> {
                }

                is CharacterListState.Error -> {
                    view.loadingView.isVisible = false
                }
            }
        }

        private fun getErrorMessage(throwable: Throwable) = view.context.getString(
            when (throwable) {
                is IOException -> R.string.network_error
                else -> R.string.generic_error
            }
        )
    }
}