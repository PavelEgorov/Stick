package com.egorovsoft.stick.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egorovsoft.stick.activitys.note.NoteResult
import com.egorovsoft.stick.data.Note
import com.egorovsoft.stick.data.User
import com.egorovsoft.stick.data.errors.NoAuthException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

/// provader
class FireStoreProvider(private val firebaseAuth: FirebaseAuth, /// mockk<FirebaseAuth>
                        private val store: FirebaseFirestore /// mockk<FirebaseFirestore>
) : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USER_COLLECTION = "users"
    }

    private val currentUser
        get() = firebaseAuth.currentUser

    private val userNotesCollection: CollectionReference /// mockk<CollectionReference// >
        get() = currentUser?.let {
            store.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()


    /// test@'user is null'
    override fun getCurrentUser() = MutableLiveData<User?>().apply {
        value = currentUser?.let { firebaseUser ->
            User(firebaseUser.displayName ?: "", firebaseUser.email ?: "")
        }
    }

    /// test@''
    override fun subscribeToAllNotes() = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.addSnapshotListener { snapshot, e ->
                e?.let {
                    throw it
                } ?: let {
                    snapshot?.let { snapshot ->
                        value = NoteResult.Success(snapshot.map { it.toObject(Note::class.java) })
                    }
                }
            }
        } catch (e: Throwable){
            value = NoteResult.Error(e)
        }
    }

    ///test@'test note find by id'
    ///test@'test error find by id'
    override fun getNoteById(id: String) = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(id).get()
                .addOnSuccessListener { snapshot -> /// mockk<QueryListener>
                    value = NoteResult.Success(snapshot.toObject(Note::class.java)) /// assertTrue
                }.addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable){
            value = NoteResult.Error(e)
        }
    }

    ///test@'verify seveNote set'
    override fun saveNote(note: Note) = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(note.id).set(note)
                .addOnSuccessListener {
                    value = NoteResult.Success(note)
                }.addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable){
            value = NoteResult.Error(e)
        }
    }

    ///test@'verify deleteNote delete'
    override fun deleteNote(noteId: String): LiveData<NoteResult> =MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(noteId).delete()
                .addOnSuccessListener {
                    value = NoteResult.Success(null)
                }.addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable){
            value = NoteResult.Error(e)
        }
    }
}