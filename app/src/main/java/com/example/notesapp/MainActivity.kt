package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.fragments.NotesShowFragment

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBar)
		setSupportActionBar(toolbar)

		supportFragmentManager.beginTransaction().replace(R.id.container, NotesShowFragment())
			.commit()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.main_activity_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {

		when (item.itemId) {
			R.id.logout -> {
				Toast.makeText(this, "LOGOUT", Toast.LENGTH_SHORT).show()
				val pref = getSharedPreferences("Notes", MODE_PRIVATE)
				val editor = pref.edit()
				editor.putBoolean("LoggedIn", false)
				editor.apply()
				startActivity(Intent(this, LoginActivity::class.java))
				finish()
			}
		}
		return super.onOptionsItemSelected(item)
	}
}