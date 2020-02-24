package com.egorovsoft.stick.activitys.note

import com.egorovsoft.stick.base.BaseViewState
import com.egorovsoft.stick.data.Note

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}