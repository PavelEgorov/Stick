package com.egorovsoft.stick.activitys

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import com.egorovsoft.stick.R
import com.egorovsoft.stick.activitys.note.NoteActivity
import com.egorovsoft.stick.activitys.note.NoteViewModel
import com.egorovsoft.stick.adapters.MainAdapter
import com.egorovsoft.stick.data.Note
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin



class MainActivityTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
    private val model: MainViewModel = mockk(relaxed = true)
    private val noteModel: NoteViewModel = mockk(relaxed = true)
    private val viewStateLiveData = MutableLiveData<MainViewState>()

    private val testNotes = listOf(
        Note("1", "title1", "text1"),
        Note("2", "title2", "text2"),
        Note("3", "title3", "text3")
    )

    @Before
    fun setUp() {
        loadKoinModules(
            listOf(
                module {
                    viewModel(override = true) { model }
                    viewModel(override = true) { noteModel }
                    }
            )
        )

        every { model.getViewState() } returns viewStateLiveData
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }

    @After
    fun tearDown(){
        stopKoin()
    }

    @Test
    fun check_data_is_displayed(){
        onView(withId(R.id.mainRecycler)).perform(scrollToPosition<MainAdapter.NoteViewHolder>(1))
        onView(withText(testNotes[1].note)).check(matches(isDisplayed()))
    }

    @Test
    fun `check_note_screen_is_open`(){
        onView(withId(R.id.mainRecycler))
            .perform(actionOnItemAtPosition<MainAdapter.NoteViewHolder>(0, click()))

        intended(allOf(hasComponent(NoteActivity::class.java.name),
            hasExtra(EXTRA_NOTE, testNotes[0].id)))
    }
}