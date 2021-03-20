package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.database.Result
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
        application: Application,
        private val database: ElectionDao
): AndroidViewModel(application) {

    val upcomingElections = MutableLiveData<List<Election>>()

    val savedElections = MutableLiveData<List<Election>>()

    init {
        populateUpcomingElections()
        populateSavedElections()
    }

    fun populateUpcomingElections() { // or rename get?
        viewModelScope.launch {
            try {
                var listResult = CivicsApi.retrofitService.getElections().elections
                if (listResult.size > 0) {
                    upcomingElections.value = listResult
                }
            } catch (e: Exception) {
                upcomingElections.value = ArrayList()
            }
        }
    }

    fun populateSavedElections() { // or name refresh?
        viewModelScope.launch {
            val result = database.getElections()
            savedElections.value = result
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun getElectionData() {

    }

}