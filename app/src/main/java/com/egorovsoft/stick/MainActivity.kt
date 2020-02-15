package com.egorovsoft.stick

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.egorovsoft.stick.activitys.note.NoteAvtivity
import com.egorovsoft.stick.adapters.MainAdapter
import com.egorovsoft.stick.base.BaseActivity
import com.egorovsoft.stick.data.Note
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {
    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override val layoutRes = R.layout.activity_main
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        adapter = MainAdapter {note ->
            openNoteScreen(note)
        }
        mainRecycler.adapter = adapter

        fab.setOnClickListener {
            openNoteScreen(null)
        }
    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteAvtivity.getStartIntent(this, note?.id ?: null)
        startActivity(intent)
    }

    override fun renderData(data: List<Note>?) {
        data?.let {
            adapter.notes = it
        }
    }
}
