package com.egorovsoft.stick.activitys.note

import com.egorovsoft.stick.base.BaseViewState
import com.egorovsoft.stick.data.Note

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)