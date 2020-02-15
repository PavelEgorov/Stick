package com.egorovsoft.stick.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egorovsoft.stick.data.provider.FireStoreProvider
import com.egorovsoft.stick.data.provider.RemoteDataProvider
import java.util.*

object Repository {
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subsrcibeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
}
