package com.egorovsoft.stick.activitys

import com.egorovsoft.stick.base.BaseViewState
import com.egorovsoft.stick.data.Note

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null): BaseViewState<List<Note>?>(notes, error)