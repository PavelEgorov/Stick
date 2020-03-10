package com.egorovsoft.stick.activitys.splash

import com.egorovsoft.stick.base.BaseViewModel
import com.egorovsoft.stick.data.Repository
import com.egorovsoft.stick.data.errors.NoAuthException
import kotlinx.coroutines.launch

class SplashViewModel(private val notesRepository: Repository) : BaseViewModel<Boolean?>() {

    fun requestUser() {
        launch {
            notesRepository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}