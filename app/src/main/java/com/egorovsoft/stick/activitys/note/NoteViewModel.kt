package com.egorovsoft.stick.activitys.note

import androidx.lifecycle.ViewModel
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.Repository

class NoteViewModel(private val repository: Repository = Repository) : ViewModel() {


    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }
}
