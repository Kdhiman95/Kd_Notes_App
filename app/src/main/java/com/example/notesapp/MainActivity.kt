package com.example.notesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.fragments.NotesShowFragment

var noteList: ArrayList<Bundle> = ArrayList()

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		supportFragmentManager.beginTransaction().replace(R.id.container, NotesShowFragment())
			.commit()
	}
}