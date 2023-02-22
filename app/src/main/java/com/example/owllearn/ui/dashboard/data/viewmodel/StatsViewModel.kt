package com.example.owllearn.ui.dashboard.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.owllearn.ui.dashboard.data.model.Statistics

class StatsViewModel : ViewModel() {
    private val _stats = MutableLiveData<Statistics>()
    val stats: LiveData<Statistics> = _stats


    fun loadStatistics() {

    }
}