package com.egorovsoft.stick.data.provider

import androidx.lifecycle.LiveData
import com.egorovsoft.stick.activitys.note.NoteResult
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.User

interface RemoteDataProvider {
    fun subsrcibeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getCurrentUser(): LiveData<User?>
}