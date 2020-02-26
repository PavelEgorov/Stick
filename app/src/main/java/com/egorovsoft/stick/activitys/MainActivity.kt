package com.egorovsoft.stick.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.egorovsoft.stick.R
import com.egorovsoft.stick.activitys.note.NoteActivity
import com.egorovsoft.stick.activitys.splash.SplashActivity
import com.egorovsoft.stick.adapters.MainAdapter
import com.egorovsoft.stick.base.BaseActivity
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.fragments.LogoutDialog
import org.koin.android.viewmodel.ext.android.viewModel
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert

class MainActivity : BaseActivity<List<Note>?>() {
    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val model: MainViewModel by viewModel()
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
        val intent = NoteActivity.start(this, note?.id ?: null)
    }

    override fun renderData(data: List<Note>?) {
        data?.let {
            adapter.notes = it
        }
    }

    fun showLogoutDialog() {
        alert {
            titleResource = R.string.logout_dialog_title
            messageResource = R.string.logout_dialog_message
            positiveButton(R.string.logout_dialog_ok) { onLogout() }
            negativeButton(R.string.logout_dialog_cancel) { dialog -> dialog.dismiss() }
        }.show()
    }

    fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
        MenuInflater(this).inflate(R.menu.main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout -> showLogoutDialog()?.let { true }
        else -> false
    }
}
