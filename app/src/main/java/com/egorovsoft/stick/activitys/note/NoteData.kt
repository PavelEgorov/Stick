package com.egorovsoft.stick.activitys.note

import com.egorovsoft.stick.base.BaseViewState
import com.egorovsoft.stick.data.Note

data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)