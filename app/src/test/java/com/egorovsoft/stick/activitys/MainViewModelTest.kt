package com.egorovsoft.stick.activitys

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.egorovsoft.stick.activitys.note.NoteResult
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.Repository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class MainViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockknotesRepository = mockk<Repository>()
    private val mockknotesObserver = MutableLiveData<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        clearMocks(mockknotesRepository)
        every { mockknotesRepository.getNotes() } returns mockknotesObserver
        viewModel = MainViewModel(mockknotesRepository)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `should call getNotes`() {
        verify(exactly = 1) { mockknotesRepository.getNotes() }
    }

    @Test
    fun `should return notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        viewModel.getViewState().observeForever {
            result = it.data
        }
        mockknotesObserver.value = NoteResult.Success(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        mockknotesObserver.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(mockknotesObserver.hasObservers())
    }
}