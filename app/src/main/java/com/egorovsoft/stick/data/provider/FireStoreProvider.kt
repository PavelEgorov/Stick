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
import com.google.firebase.firestore.QueryDocumentSnapshot

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USER_COLLECTION = "users"
    }
    private val store by lazy { FirebaseFirestore.getInstance() }

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private val userNotesCollection: CollectionReference
        get() = currentUser?.let {                                                                                                                                                                                                                                                                                                                                      //Я копипастил код с урока и не заметил эту надпись
            store.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()


    override fun getCurrentUser() = MutableLiveData<User?>().apply {
        value = currentUser?.let { firebaseUser ->
            User(firebaseUser.displayName ?: "", firebaseUser.email ?: "")
        }
    }

    //private val noteReference = store.collection(NOTES_COLLECTION)

    override fun subsrcibeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        userNotesCollection.addSnapshotListener { snapshot, e ->
            e?.let {
                result.value = NoteResult.Error(e)
            } ?: let {
                snapshot?.let { snapshot ->
                    val notes = mutableListOf<Note>()
                    for (doc: QueryDocumentSnapshot in snapshot) {
                        notes.add(doc.toObject(Note::class.java))
                    }
                    result.value = NoteResult.Success(notes)
                }
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        userNotesCollection.document(id).get()
            .addOnSuccessListener { snapshot ->
                result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
            }.addOnFailureListener {
                result.value = NoteResult.Error(it)
            }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        userNotesCollection.document(note.id).set(note)
            .addOnSuccessListener {
                result.value = NoteResult.Success(note)
            }.addOnFailureListener {
                result.value = NoteResult.Error(it)
            }

        return result
    }
}