package com.egorovsoft.stick.activitys

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
import com.egorovsoft.stick.data.Repository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
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
    private val viewStateChannel = BroadcastChannel<List<Note>?>(Channel.CONFLATED)
    private val testNotes = listOf(
        Note("afafaf", "first title", "first body"),
        Note("bfbfbf", "second title", "second body"),
        Note("cfcfcf", "third title", "third body")
    )

    @Before
    fun setUp() {
        loadKoinModules(
            listOf(
                module {
                    viewModel(override = true) { model }
                    viewModel(override = true) { mockk<NoteViewModel>(relaxed = true) }
                }
            )
        )
        every { model.getViewState() } returns viewStateChannel.openSubscription()
        activityTestRule.launchActivity(null)
        runBlocking {
            viewStateChannel.send(testNotes)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        viewStateChannel.close()
    }

    @Test
    fun check_data_is_displayed() {
        onView(withId(R.id.mainRecycler)).perform(scrollToPosition<MainAdapter.NoteViewHolder>(1))
        onView(withText(testNotes[1].note)).check(matches(isDisplayed()))
    }

    @Test
    fun check_detail_activity_intent_sent() {
        onView(withId(R.id.mainRecycler)).perform(actionOnItemAtPosition<MainAdapter.NoteViewHolder>(1, click()))
        intended(allOf(hasComponent(NoteActivity::class.java.name), hasExtra(EXTRA_NOTE, testNotes[1].id)))
    }
}