package com.egorovsoft.stick.activitys.note

import androidx.annotation.VisibleForTesting
import com.egorovsoft.stick.base.BaseViewModel
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.Repository
import kotlinx.coroutines.launch

class NoteViewModel(private val notesRepository: Repository) : BaseViewModel<NoteData>() {

    private val pendingNote: Note?
        get() = getViewState().poll()?.note

    fun save(note: Note) {
        setData(NoteData(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                notesRepository.getNoteById(noteId).let {
                    setData(NoteData(note = it))
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteNote() {
        pendingNote?.let { note ->
            launch {
                try {
                    notesRepository.deleteNote(note.id)
                    setData(NoteData(isDeleted = true))
                } catch (e: Throwable) {
                    setError(e)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            pendingNote?.let {
                try {
                    notesRepository.saveNote(it)
                } catch (e: Throwable) {
                    setError(e)
                }
            }
            super.onCleared()
        }
    }
}