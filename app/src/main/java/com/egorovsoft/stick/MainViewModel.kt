package com.egorovsoft.stick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    private val stateLiveData : MutableLiveData<String> = MutableLiveData();

    init {
        stateLiveData.value = "Hello teacher!"
    }

    fun viewState(): LiveData<String> = stateLiveData;
    fun viewUpdate() {
        stateLiveData.value = "Ups! ^_^"
    }
}