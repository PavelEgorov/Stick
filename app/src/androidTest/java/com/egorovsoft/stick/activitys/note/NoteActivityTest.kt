package com.egorovsoft.stick.activitys.note

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.egorovsoft.stick.R
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.getColorInt
import io.mockk.*
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin

class NoteActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)
    private val model: NoteViewModel = mockk(relaxed = true)
    private val viewStateChannel = BroadcastChannel<NoteData>(Channel.CONFLATED)
    private val testNote = Note("fach432", "test note", "test body")

    @Before
    fun setUp() {
        loadKoinModules(listOf(module { viewModel(override = true) { model } }))
        every { model.getViewState() } returns viewStateChannel.openSubscription()
        every { model.loadNote(any()) } just runs
        every { model.save(any()) } just runs
        every { model.deleteNote() } just runs

        Intent().apply {
            putExtra(NoteActivity::class.java.name + "extra.NOTE", testNote.id)
        }.let {
            activityTestRule.launchActivity(it)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_show_color_palette() {
        onView(withId(R.id.palette)).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_palette() {
        onView(withId(R.id.palette)).perform(click()).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        onView(withId(R.id.palette)).perform(click())
        onView(withTagValue(`is`(Note.Color.BLUE))).perform(click())

        val colorInt = Note.Color.BLUE.getColorInt(activityTestRule.activity)

        onView(withId(R.id.noteToolbar)).check { view, noViewFoundException ->
            assertTrue((view.background as? ColorDrawable)?.color == colorInt)
        }
    }

    @Test
    fun should_all_viewModel_loadNote_once() {
        verify(exactly = 1) { model.loadNote(testNote.id) }
    }

    @Test
    fun should_show_note() {
        activityTestRule.launchActivity(null)
        runBlocking {
            viewStateChannel.send(NoteData(note = testNote))
        }

        onView(withId(R.id.txtNoteTitle)).check(matches(withText(testNote.title)))
        onView(withId(R.id.txtNoteBody)).check(matches(withText(testNote.note)))
    }

    @Test
    fun should_call_save_note() {
        activityTestRule.launchActivity(null)
        onView(withId(R.id.txtNoteTitle)).perform(typeText(testNote.title))
        verify(timeout = 1000) { model.save(any()) }
    }

    @Test
    fun should_call_delete_note() {
        openActionBarOverflowOrOptionsMenu(activityTestRule.activity)
        onView(withText(R.string.note_delete)).perform(click())
        onView(withText(R.string.logout_dialog_ok)).perform(click())
        verify { model.deleteNote() }
    }

}