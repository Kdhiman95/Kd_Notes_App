package com.example.notesapp.modal.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDAO {
	@Query("SELECT * FROM notes_table")
	suspend fun getNotes(): List<Note>

	@Insert
	suspend fun insertNote(note: Note)

	@Query("UPDATE notes_table SET title=:title,note=:note,curDate=:curDate WHERE id=:id")
	suspend fun updateNote(id: Int, title: String, note: String, curDate: String)

	@Delete
	suspend fun delete(note: Note)
}