package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import kotlinx.coroutines.launch

class VoterInfoViewModel(
        electionId: Int, division: Division, private val dataSource: ElectionDao
) : ViewModel() {

    private val _selectedElection = MutableLiveData<Election>()
    val selectedElection: LiveData<Election>
        get() = _selectedElection

    private val _votingLocations = MutableLiveData<String>()
    val votingLocations: LiveData<String>
        get() = _votingLocations

    private val _ballotInfo = MutableLiveData<String>()
    val ballotInfo: LiveData<String>
        get() = _ballotInfo

    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

    private val _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean>
        get() = _saved

    init {
        populateVoterInfo(electionId, division)
        getSavedValue(electionId)
    }

    fun populateVoterInfo(electionId: Int, division: Division) {
        viewModelScope.launch {
            try {
                var address = division.state + ", " + division.country
                var voterInfoResponse = CivicsApi.retrofitService.getVoterInfo(address, electionId)
                _selectedElection.value = voterInfoResponse.election
                voterInfoResponse.state?.let { state ->
                    if (state.isNotEmpty()) {
                        val adminBody = state[0].electionAdministrationBody
                        adminBody.let {
                            _votingLocations.value = it.votingLocationFinderUrl
                            _ballotInfo.value = it.ballotInfoUrl
                            _address.value = it.correspondenceAddress?.toFormattedString()
                        }
                    }
                }
            } catch (e: Exception) {
                _selectedElection.value = null
                _votingLocations.value = null
                _ballotInfo.value = null
                _address.value = null
            }
        }
    }

    fun getSavedValue(electionId: Int) {
        viewModelScope.launch {
            var election = dataSource.getElectionById(electionId)
            _saved.value = election != null
        }
    }

    fun saveElection() {
        viewModelScope.launch {
            dataSource.saveElection(_selectedElection.value!!)
        }
        _saved.value = true
    }

    fun removeElection() {
        viewModelScope.launch {
            dataSource.deleteElection(_selectedElection.value!!.id)
        }
        _saved.value = false
    }
}