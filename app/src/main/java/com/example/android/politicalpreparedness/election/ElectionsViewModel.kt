package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class ElectionsViewModel(
        application: Application,
        private val database: ElectionDao
): AndroidViewModel(application) {

    val upcomingElections = MutableLiveData<List<Election>>()

    val savedElections = MutableLiveData<List<Election>>() // FIXME how to update this when it changes

    private val _navigateToSelectedElection = MutableLiveData<Election>()
    val navigateToSelectedElection: LiveData<Election>
        get() = _navigateToSelectedElection

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

    fun displayElectionDetails(election: Election) {
        _navigateToSelectedElection.value = election
    }

    fun displayElectionComplete() {
        _navigateToSelectedElection.value = null
    }
}