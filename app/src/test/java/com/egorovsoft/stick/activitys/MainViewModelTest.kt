package com.egorovsoft.stick.activitys

import com.egorovsoft.stick.activitys.note.NoteResult
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.Repository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private val mockRepository = mockk<Repository>()
    private val notesReceiveChannel = Channel<NoteResult>(Channel.CONFLATED)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        clearMocks(mockRepository)
        every { mockRepository.getNotes() } returns notesReceiveChannel
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return Notes`() {
        val testData = listOf(Note("1"), Note("2"))
        runBlocking {
            notesReceiveChannel.send(NoteResult.Success(testData))
            val result = viewModel.getViewState().receive()
            assertEquals(testData, result)
        }
    }

    @Test
    fun `should return error`() {
        val testData = Throwable("error")
        runBlocking {
            notesReceiveChannel.send(NoteResult.Error(testData))
            val result = viewModel.getErrorChannel().receive()
            assertEquals(testData, result)
        }
    }

    @Test
    fun `should cancel channel`(){
        viewModel.onCleared()
        assertTrue(notesReceiveChannel.isClosedForReceive)
    }
}