package com.example.notesapp.view_modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.modal.repository.NoteRepository


class NoteViewModelFactory(private val noteRepository: NoteRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return NoteViewModel(noteRepository) as T
	}
}