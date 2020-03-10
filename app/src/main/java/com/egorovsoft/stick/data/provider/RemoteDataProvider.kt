package com.egorovsoft.stick.data.provider

import com.egorovsoft.stick.activitys.note.NoteResult
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.User
import kotlinx.coroutines.channels.ReceiveChannel

interface RemoteDataProvider {
    fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note): Note
    suspend fun getCurrentUser(): User?
    suspend fun deleteNote(noteId: String)
}