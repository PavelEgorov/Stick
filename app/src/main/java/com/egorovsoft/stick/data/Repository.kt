package com.egorovsoft.stick.data

import com.egorovsoft.stick.data.provider.RemoteDataProvider

class Repository (val remoteProvider: RemoteDataProvider){
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
    fun deleteNote(id: String) = remoteProvider.deleteNote(id)
}