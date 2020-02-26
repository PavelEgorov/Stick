package com.egorovsoft.stick.data

import com.egorovsoft.stick.data.provider.RemoteDataProvider

class Repository(val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    suspend fun saveNote(note: Note) = remoteProvider.saveNote(note)
    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()
    suspend fun deleteNote(id: String) = remoteProvider.deleteNote(id)
}