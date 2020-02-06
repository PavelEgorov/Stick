package com.egorovsoft.stick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.egorovsoft.stick.data.Repository

class MainViewModel : ViewModel(){
    private val stateLiveData : MutableLiveData<MainViewState> = MutableLiveData();

    init {
        Repository.getNotes().observeForever {notes ->
            stateLiveData.value = stateLiveData.value?.copy(notes = notes) ?: MainViewState(notes)
        }

    }

    fun viewState(): LiveData<MainViewState> = stateLiveData;
}