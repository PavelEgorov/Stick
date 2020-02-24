package com.egorovsoft.stick.activitys.splash

import com.egorovsoft.stick.base.BaseViewModel
import com.egorovsoft.stick.data.Repository
import com.egorovsoft.stick.data.errors.NoAuthException

class SplashViewModel(private val notesRepository: Repository) : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        notesRepository.getCurrentUser().observeForever {                                                                                                                                                                                                                           //Я копипастил код с урока и не заметил эту надпись
            viewStateLiveData.value = it?.let {
                SplashViewState(authenticated = true)
            } ?: let {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}