package com.egorovsoft.stick.activitys.note

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.egorovsoft.stick.R
import com.egorovsoft.stick.data.Note
import kotlinx.android.synthetic.main.activity_note_avtivity.*
import java.text.SimpleDateFormat
import java.util.*
import com.egorovsoft.stick.data.Note.Color
import com.egorovsoft.stick.extensions.DATE_TIME_FORMAT

private const val SAVE_DELAY = 2000L

class NoteAvtivity : AppCompatActivity() {

    companion object {
        private val EXTRA_NOTE = NoteAvtivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, note: Note?): Intent {
            val intent = Intent(context, NoteAvtivity::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            return intent
        }
    }

    private var note: Note? = null
    private lateinit var viewModel: NoteViewModel

    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            triggerSaveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // not used
        }
    }

    private fun triggerSaveNote() {

        if (txtNoteTitle.text == null || txtNoteBody.text!!.length < 3) return

        Handler().postDelayed({
                note = note?.copy(title = txtNoteTitle.text.toString(),
                    note = txtNoteBody.text.toString(),
                    lastChanged = Date())
                    ?: createNewNote()

                note?.let { viewModel.saveChanges(it)}

        }, SAVE_DELAY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_avtivity)

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        setSupportActionBar(noteToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = if (note != null) {
            SimpleDateFormat(DATE_TIME_FORMAT,
                Locale.getDefault()).format(note!!.lastChanged)
        } else {
            getString(R.string.new_note_title)
        }

        initView()
    }

    private fun initView() {
        note?.let {
            txtNoteTitle.setText(it.title)
            txtNoteBody.setText(it.note)
            val color = when (it.color) {
                Color.WHITE -> R.color.color_white
                Color.YELLOW -> R.color.color_yello
                Color.RED -> R.color.color_red
                Color.GREEN -> R.color.color_green
                Color.BLUE -> R.color.color_blue
                else -> R.color.color_green
            }

            noteToolbar.setBackgroundColor(ContextCompat.getColor(this, color))
        }
        txtNoteTitle.addTextChangedListener(textChangeListener)
        txtNoteBody.addTextChangedListener(textChangeListener)
    }

    private fun createNewNote(): Note = Note(UUID.randomUUID().toString(), txtNoteTitle.text.toString(), txtNoteBody.text.toString())

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
