package com.example.notesapp.modal.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val title: String,
	val note: String,
	val curDate: String,
)
