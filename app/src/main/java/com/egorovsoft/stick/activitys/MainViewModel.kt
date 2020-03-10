package com.egorovsoft.stick.activitys

import androidx.annotation.VisibleForTesting
import com.egorovsoft.stick.activitys.note.NoteResult
import com.egorovsoft.stick.base.BaseViewModel
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.Repository
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainViewModel(notesRepository: Repository) : BaseViewModel<List<Note>?>() {

    private val notesChannel = notesRepository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when(it){
                    is NoteResult.Success<*> -> setData(it.data as? List<Note>)
                    is NoteResult.Error -> setError(it.error)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}
