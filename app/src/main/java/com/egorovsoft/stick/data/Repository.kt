package com.egorovsoft.stick.data

import com.egorovsoft.stick.data.provider.FireStoreProvider
import com.egorovsoft.stick.data.provider.RemoteDataProvider

object Repository {
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subsrcibeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
}
