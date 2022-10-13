package com.example.notesapp

import android.app.Application
import com.example.notesapp.modal.repository.NoteRepository
import com.example.notesapp.modal.room_database.NoteDatabase

class NoteApplication : Application() {

	lateinit var noteRepository: NoteRepository
	override fun onCreate() {
		super.onCreate()
		initialize()
	}

	private fun initialize() {
		val noteDatabase = NoteDatabase.getDatabase(applicationContext)
		noteRepository = NoteRepository(noteDatabase)
	}
}