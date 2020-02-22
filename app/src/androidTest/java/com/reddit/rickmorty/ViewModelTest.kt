package com.reddit.rickmorty

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.reddit.rickmorty.domain.FetchCharactersUseCase
import com.reddit.rickmorty.ui.CharacterListState
import com.reddit.rickmorty.ui.CharactersListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @MockK
    lateinit var useCase: FetchCharactersUseCase

    lateinit var viewModel: CharactersListViewModel

    private val contextProvider: CoroutineContextProvider =
        CoroutineContextProviderTest()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CharactersListViewModel(contextProvider, useCase)
        viewModel.status.observeForever {}
    }
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.reddit.rickmorty", appContext.packageName)
    }

    @Test
    fun no_fetching_returns_no_state() {
        assertNull(viewModel.status.value)
    }

    @Test
    fun empty_list_returns_data_state() {
        coEvery { useCase.getCharacters(1) } returns emptyList()
        viewModel.loadList(1) {}
        val result = viewModel.status.value as? CharacterListState.Data
        assertNotNull(result)
    }

    @Test
    fun exception_in_fetch_returns_error_state() {
        val errorMessage = "test exception"
        coEvery { useCase.getCharacters(1) } throws Exception(errorMessage)
        viewModel.loadList(1) {}
        val result = viewModel.status.value as? CharacterListState.Error
        assertNotNull(result)
        assertEquals(errorMessage, result?.throwable?.message)
    }
}
