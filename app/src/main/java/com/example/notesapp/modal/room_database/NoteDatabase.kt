package com.example.notesapp.modal.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
	abstract fun noteDAO(): NotesDAO

	companion object {

		private var INSTANCE: NoteDatabase? = null

		fun getDatabase(context: Context): NoteDatabase {
			if (INSTANCE == null) {
				synchronized(this) {
					INSTANCE = Room.databaseBuilder(
						context,
						NoteDatabase::class.java,
						"notes_database"
					).build()
				}
			}
			return INSTANCE!!
		}

	}
}