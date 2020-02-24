package com.egorovsoft.stick.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.egorovsoft.stick.activitys.note.NoteResult
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.errors.NoAuthException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.auth.User
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class FireStoreProviderTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockkStore = mockk<FirebaseFirestore>()
    private val mockkAuth = mockk<FirebaseAuth>()
    private val mockkCollection = mockk<CollectionReference>()
    private val mockkUser = mockk<FirebaseUser>()

    private val mockkDocument1 = mockk<DocumentSnapshot>()
    private val mockkDocument2 = mockk<DocumentSnapshot>()
    private val mockkDocument3 = mockk<DocumentSnapshot>()

    private val notesList = listOf<Note>(
        Note("1"),
        Note("2"),
        Note("3")
    )

    private val privader = FireStoreProvider(mockkAuth, mockkStore)

    @Before
    fun setUp() {
        clearAllMocks()
        every { mockkAuth.currentUser } returns mockkUser
        every { mockkUser.uid } returns ""
        every {mockkStore.collection(any()).document(any()).collection(any())} returns mockkCollection

        every { mockkDocument1.toObject(Note::class.java) } returns notesList[0]
        every { mockkDocument2.toObject(Note::class.java) } returns notesList[1]
        every { mockkDocument3.toObject(Note::class.java) } returns notesList[2]
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getCurrentUser() {
        var result: Any? = null
        every { mockkAuth.currentUser } returns null
        privader.subscribeToAllNotes().observeForever{
            result = (it as? NoteResult.Error)?.error
        }

        assertTrue(result is NoAuthException)
    }

    @Test
    fun subscribeToAllNotes() {
        var result: List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockSnapshot.documents } returns listOf(mockkDocument1, mockkDocument2, mockkDocument3)
        every { mockkCollection.addSnapshotListener(capture(slot)) } returns mockk()
        privader.subscribeToAllNotes().observeForever{
            result = (it as? NoteResult.Success<List<Note>>)?.data
        }
        slot.captured.onEvent(mockSnapshot, null)
        assertEquals(notesList, result)
    }

    @Test
    fun `test event fun getNoteById`() {
        val mockkSnapshot = mockk<DocumentSnapshot>()
        val slot = slot<OnSuccessListener<DocumentSnapshot>>()
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()

        every { mockkSnapshot.toObject(Note::class.java) } returns notesList[0]
        every { mockkCollection.document(notesList[0].id) } returns mockDocumentReference
        every { mockkCollection.document(notesList[0].id).get().addOnSuccessListener(capture(slot)) } returns mockk()

        privader.getNoteById(notesList[0].id).observeForever {
            result = (it as? NoteResult.Success<Note>)?.data
        }

        verify(exactly = 1) { mockkCollection.document(notesList[0].id).get() }

        slot.captured.onSuccess(
            mockkSnapshot
        )

        assertNotNull(result)
        assertEquals(notesList[0], result)
    }

    @Test
    fun saveNote() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockkCollection.document(notesList[0].id) } returns mockDocumentReference
        privader.saveNote(notesList[0])
        verify(exactly = 1) { mockDocumentReference.set(notesList[0]) }
    }

    @Test
    fun deleteNote() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockkCollection.document(notesList[0].id) } returns mockDocumentReference
        privader.deleteNote(notesList[0].id)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }
}