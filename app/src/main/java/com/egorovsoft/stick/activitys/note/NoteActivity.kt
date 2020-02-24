package com.egorovsoft.stick.activitys.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.egorovsoft.stick.R
import com.egorovsoft.stick.base.BaseActivity
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.getColorInt
import kotlinx.android.synthetic.main.activity_note_avtivity.*
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {
    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            context.startActivity(intent)
        }
    }

    override val layoutRes = R.layout.activity_note_avtivity
    override val model: NoteViewModel by viewModel()
    private var note: Note? = null
    var color = Note.Color.WHITE

    val textChahgeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(noteToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)

        noteId?.let {
            model.loadNote(it)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
            initView()
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()
        this.note = data.note
        initView()
    }

    fun initView() {
        note?.let { note ->
            removeEditListener()
            txtNoteTitle.setText(note.title)
            txtNoteBody.setText(note.note)
            noteToolbar.setBackgroundColor(note.color.getColorInt(this))
            supportActionBar?.title = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note.lastChanged)
        } ?: let {
            supportActionBar?.title =   getString(R.string.new_note_title)
        }

        setEditListener()

        colorPicker.onColorClickListener = {
            noteToolbar.setBackgroundColor(color.getColorInt(this))
            color = it
            saveNote()
        }
    }

    private fun removeEditListener(){
        txtNoteTitle.removeTextChangedListener(textChahgeListener)
        txtNoteBody.removeTextChangedListener(textChahgeListener)
    }

    private fun setEditListener(){
        txtNoteTitle.removeTextChangedListener(textChahgeListener)
        txtNoteBody.removeTextChangedListener(textChahgeListener)
    }


    fun saveNote() {
        if (txtNoteTitle.text == null || txtNoteTitle.text!!.length < 3) return

        note = note?.copy(
            title = txtNoteTitle.text.toString(),
            note = txtNoteBody.text.toString(),
            lastChanged = Date(),
            color = color
        ) ?: Note(
            UUID.randomUUID().toString(),
            txtNoteTitle.text.toString(),
            txtNoteBody.text.toString(),
            color
        )

        note?.let {
            model.save(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = menuInflater.inflate(R.menu.note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    private fun deleteNote() {
        alert {
            messageResource = R.string.logout_dialog_message
            negativeButton(R.string.logout_dialog_cancel) { dialog -> dialog.dismiss() }
            positiveButton(R.string.logout_dialog_ok) { model.deleteNote() }
        }.show()
    }
}