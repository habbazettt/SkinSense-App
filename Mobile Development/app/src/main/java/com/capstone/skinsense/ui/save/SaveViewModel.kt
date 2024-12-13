package com.capstone.skinsense.ui.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.skinsense.data.local.PredictionResult
import com.capstone.skinsense.data.local.PredictionResultDao

class SaveViewModel(private val dao: PredictionResultDao) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is save Fragment"
    }
    val text: LiveData<String> = _text

    fun getAllResults(): LiveData<List<PredictionResult>> = dao.getAllResults()
}