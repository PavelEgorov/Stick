package com.egorovsoft.stick.activitys.note

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
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
    private val viewStateLiveData = MutableLiveData<NoteData>()

    private val testNote = Note("1", "title1", "text1")

    @Before
    fun setUp() {
        loadKoinModules(
            listOf(
                module {
                    viewModel(override = true) { model }
                }
            )
        )

        every { model.getViewState() } returns viewStateLiveData
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
    fun should_show_color_picker() {
        onView(withId(R.id.palette)).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker() {
        onView(withId(R.id.palette)).perform(click()).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(not(isDisplayed())))
    }


    @Test
    fun should_set_toolbar_color() {
        onView(withId(R.id.palette)).perform(click())
        onView(withTagValue(`is`(Note.Color.BLUE))).perform(click())

        val colorInt = Note.Color.BLUE.getColorInt(activityTestRule.activity)

        onView(withId(R.id.noteToolbar)).check {view, _ ->
            ///androidx.test.espresso.base.DefaultFailureHandler$AssertionFailedWithCauseError. В Коде нашел ошибку, цвет присваивался предыдущий.
            // Но после исправления ошибка не ушла
            assertTrue((view.background as? ColorDrawable)?.color == colorInt)
        }
    }

    @Test
    fun should_call_viewModel_loadNote() {
        verify(exactly = 1) { model.loadNote(testNote.id) }
    }

    @Test
    fun should_show_note() {
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(NoteData(NoteData.Data(note = testNote)))

        onView(withId(R.id.txtNoteTitle)).check(matches(withText(testNote.title)))
        onView(withId(R.id.txtNoteBody)).check(matches(withText(testNote.note)))
    }

    @Test
    fun should_call_saveNote() {
        onView(withId(R.id.txtNoteTitle)).perform(typeText(testNote.title))
        ///Verification failed: call 1 of 1: NoteViewModel(#1).save(any())) was not called. Была ошибка в коде. Но после исправления по прежнему пишет, что
        // save не был вызван. Думаю проблема в txtNoteTitle.addTextChangedListener. Для него нужно написать slot. Но пока не могу придумать как.
        verify(timeout = 10000) { model.save(any()) }
    }

    @Test
    fun should_call_deleteNote() {
        onView(withId(R.id.palette)).perform(click())
        onView(withId(R.id.delete)).perform(click())
        onView(withText(R.string.logout_dialog_ok)).perform(click())
        verify(exactly = 1) { model.deleteNote() }
    }
}
