package com.example.notesapp

import android.os.Bundle
import androidx.lifecycle.ViewModel

class Data : ViewModel() {
    private val noteList : ArrayList<Bundle> = ArrayList()
    fun setData(bundle : Bundle){
        noteList.add(bundle)
    }
    fun getData(): ArrayList<Bundle>{
        return noteList
    }
}