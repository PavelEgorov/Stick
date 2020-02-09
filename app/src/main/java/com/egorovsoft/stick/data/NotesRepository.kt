package com.egorovsoft.stick.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

object Repository {
    private val notesLiveData = MutableLiveData<List<Note>>()
    private val notes: MutableList<Note> = mutableListOf(
        Note(UUID.randomUUID().toString(),
            "Моя первая заметка",
            "Kotlin очень краткий, но при этом выразительный язык",
            Note.Color.BLUE),
        //0xfff06292.toInt()),
        Note(UUID.randomUUID().toString(),
            "Моя первая заметка",
            "Kotlin очень краткий, но при этом выразительный язык",
            Note.Color.WHITE),//0xff9575cd.toInt()),
        Note(UUID.randomUUID().toString(),
            "Моя первая заметка",
            "Kotlin очень краткий, но при этом выразительный язык",
            Note.Color.YELLOW),//0xff64b5f6.toInt()),
        Note(UUID.randomUUID().toString(),
            "Моя первая заметка",
            "Kotlin очень краткий, но при этом выразительный язык",
            Note.Color.GREEN),//0xff4db6ac.toInt()),
        Note(UUID.randomUUID().toString(),
            "Моя первая заметка",
            "Kotlin очень краткий, но при этом выразительный язык",
            Note.Color.BLACK),//0xffb2ff59.toInt()),
        Note(UUID.randomUUID().toString(),
            "Моя первая заметка",
            "Kotlin очень краткий, но при этом выразительный язык",
            Note.Color.RED),//0xffffeb3b.toInt()),
        Note(UUID.randomUUID().toString(),
            "Моя первая заметка",
            "Kotlin очень краткий, но при этом выразительный язык",
            Note.Color.GREEN)
    )

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {

        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes.set(i, note)
                return
            }
        }

        notes.add(note)
    }
}
