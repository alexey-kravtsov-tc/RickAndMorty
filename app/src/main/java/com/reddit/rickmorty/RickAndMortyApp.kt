package com.reddit.rickmorty

import android.app.Application
import com.reddit.rickmorty.domain.FetchCharactersUseCase
import com.reddit.rickmorty.model.CharactersApiInterface
import com.reddit.rickmorty.model.dto.CharacterDto
import com.reddit.rickmorty.ui.CharacterDetailsViewModel
import com.reddit.rickmorty.ui.CharactersListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RickAndMortyApp : Application() {

    override fun onCreate() {

        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RickAndMortyApp)
            modules(appModule)
        }
    }
    private val appModule = module {

        single<Retrofit> {
            Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        factory { get<Retrofit>().create(CharactersApiInterface::class.java) }

        single { FetchCharactersUseCase(get(), get()) }

        single<CoroutineContextProvider> { CoroutineContextProviderLive() }

        viewModel { CharactersListViewModel(get(), get()) }
        viewModel { (c: CharacterDto) -> CharacterDetailsViewModel(c) }
    }

}