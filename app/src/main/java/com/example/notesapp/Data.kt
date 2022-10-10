package com.example.notesapp

import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Data(context: Context) {

	companion object {
		var noteList: ArrayList<Bundle> = ArrayList()
	}

	private val pref = context.getSharedPreferences("Notes", Context.MODE_PRIVATE)

	fun saveData() {
		val editor = pref.edit()
		val gson = Gson()
		editor.putString("Note", gson.toJson(noteList))
		editor.apply()
	}

	private fun getSaveData(): ArrayList<Bundle> {
		val gson = Gson()
		val type = object : TypeToken<ArrayList<Bundle>>() {}.type
		val noteString = pref.getString("Note", null)
		return if (noteString == null) {
			noteList
		} else {
			gson.fromJson(noteString, type)
		}
	}

	fun setData(bundle: Bundle) {
		noteList.add(bundle)
		saveData()
	}

	fun updateData(bundle: Bundle, position: Int) {
		noteList[position] = bundle
		saveData()
	}

	fun getData(): ArrayList<Bundle> {
		noteList = getSaveData()
		return noteList
	}
}