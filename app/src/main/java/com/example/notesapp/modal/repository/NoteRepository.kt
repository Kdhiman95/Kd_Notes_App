package com.example.notesapp.modal.repository

import com.example.notesapp.modal.room_database.Note
import com.example.notesapp.modal.room_database.NoteDatabase

class NoteRepository(private val noteDatabase: NoteDatabase) {

	suspend fun getNoteList(): List<Note> {
		return noteDatabase.noteDAO().getNotes()
	}

	suspend fun insertNote(note: Note) {
		noteDatabase.noteDAO().insertNote(note)
	}

	suspend fun updateNote(id: Int, title: String, note: String, curDate: String) {
		noteDatabase.noteDAO().updateNote(id, title, note, curDate)
	}

	suspend fun deleteNote(note: Note) {
		noteDatabase.noteDAO().delete(note)
	}
}