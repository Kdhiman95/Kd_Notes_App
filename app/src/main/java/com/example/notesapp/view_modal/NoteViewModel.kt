package com.example.notesapp.view_modal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.modal.repository.NoteRepository
import com.example.notesapp.modal.room_database.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {

	private val noteMutableList = MutableLiveData<List<Note>>()

	val noteList: LiveData<List<Note>>
		get() = noteMutableList

	init {
		getNotes()
	}

	private fun getNotes() {
		viewModelScope.launch(Dispatchers.IO) {
			noteMutableList.postValue(noteRepository.getNoteList())
		}
	}

	fun insertNote(note: Note) {
		viewModelScope.launch(Dispatchers.IO) {
			noteRepository.insertNote(note)
		}
	}

	fun updateNote(id: Int, title: String, note: String, curDate: String) {
		viewModelScope.launch(Dispatchers.IO) {
			noteRepository.updateNote(id, title, note, curDate)
		}
	}

	fun deleteNote(note: Note) {
		viewModelScope.launch(Dispatchers.IO) {
			noteRepository.deleteNote(note)
		}
	}
}