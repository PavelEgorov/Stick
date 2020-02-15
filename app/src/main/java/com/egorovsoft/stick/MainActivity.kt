package com.egorovsoft.stick

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.egorovsoft.stick.activitys.note.NoteAvtivity
import com.egorovsoft.stick.activitys.splash.SplashActivity
import com.egorovsoft.stick.adapters.MainAdapter
import com.egorovsoft.stick.base.BaseActivity
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.fragments.LogoutDialog
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<Note>?, MainViewState>(), LogoutDialog.LogoutListener {
    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

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

    fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG) ?:
        LogoutDialog.createInstance().show(supportFragmentManager, LogoutDialog.TAG)
    }

    override fun onLogout() {
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
