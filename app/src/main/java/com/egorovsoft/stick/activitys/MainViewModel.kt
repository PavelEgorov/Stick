package com.egorovsoft.stick.activitys

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.egorovsoft.stick.activitys.note.NoteResult
import com.egorovsoft.stick.base.BaseViewModel
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.Repository

class MainViewModel(private val notesRepository: Repository) : BaseViewModel<List<Note>?, MainViewState>() {
    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            t ?: return

            when(t){
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value =
                        MainViewState(notes = t.data as? List<Note>)
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value =
                        MainViewState(error = t.error)
                }
            }
        }
    }

    private val repositoryNotes = notesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData;

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
        super.onCleared()
        println("onCleared")
    }
}
