package com.egorovsoft.stick.activitys.note

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.egorovsoft.stick.base.BaseViewModel
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.Repository

class NoteViewModel() : BaseViewModel<Note?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    fun loadNote(noteId: String) {
        Repository.getNoteById(noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(t: NoteResult?) {
                t ?: return
                when (t) {
                    is NoteResult.Success<*> -> {
                        viewStateLiveData.value = NoteViewState(note = t.data as Note)
                    }
                    is NoteResult.Error -> {
                        viewStateLiveData.value = NoteViewState(error = t.error)
                    }
                }
            }
        })
    }

    override fun onCleared() {
        pendingNote?.let {
            Repository.saveNote(it)
        }
    }
}
