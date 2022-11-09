package com.example.notesapp.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.notesapp.modal.repository.NoteRepository
import com.example.notesapp.modal.room_database.NoteDatabase

class NoteApplication : Application() {

	lateinit var noteRepository: NoteRepository
	override fun onCreate() {
		super.onCreate()
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		initialize()
	}

	private fun initialize() {
		val noteDatabase = NoteDatabase.getDatabase(applicationContext)
		noteRepository = NoteRepository(noteDatabase)
	}
}