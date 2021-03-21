package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment: Fragment() {

    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireActivity().application
        val dataSource = ElectionDatabase.getInstance(application).electionDao
        val viewModelFactory = ElectionsViewModelFactory(application, dataSource)
        viewModel =
                ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)

        val binding = FragmentElectionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.electionsViewModel = viewModel

        val upcomingElectionListAdapter = ElectionListAdapter(ElectionListener {
            viewModel.displayElectionDetails(it)
        })
        binding.upcomingElectionsRecycler.adapter = upcomingElectionListAdapter
        binding.upcomingElectionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.let {
                upcomingElectionListAdapter.submitList(elections)
            }
        })

        val savedElectionListAdapter = ElectionListAdapter(ElectionListener {
            viewModel.displayElectionDetails(it)
        })
        binding.savedElectionsRecycler.adapter = savedElectionListAdapter
        binding.savedElectionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.savedElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.let {
                savedElectionListAdapter.submitList(elections)
            }
        })

        viewModel.navigateToSelectedElection.observe(viewLifecycleOwner, Observer { election ->
            if (election != null) {
                this.findNavController().navigate(
                        ElectionsFragmentDirections.actionShowVoterInfo(election.id, election.division)
                )
                viewModel.displayElectionComplete()
            }
        })
        return binding.root
    }
}