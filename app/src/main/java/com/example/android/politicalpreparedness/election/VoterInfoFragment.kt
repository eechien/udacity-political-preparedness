package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val args = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = args.argElectionId
        val division = args.argDivision

        val application = requireActivity().application
        val dataSource = ElectionDatabase.getInstance(application).electionDao
        val viewModelFactory = VoterInfoViewModelFactory(electionId, division, dataSource)
        val viewModel =
                ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel
        
        viewModel.ballotInfo.observe(viewLifecycleOwner, Observer { ballotInfo ->
             binding.stateBallot.isInvisible = ballotInfo == null
        })
        viewModel.votingLocations.observe(viewLifecycleOwner, Observer { votingLocations ->
            binding.stateLocations.isInvisible = votingLocations == null
        })
        viewModel.address.observe(viewLifecycleOwner, Observer { address ->
            binding.addressGroup.isInvisible = address == null
        })

        binding.stateLocations.setOnClickListener {
            val url = viewModel.votingLocations.value
            if (url != null) {
                loadWebsite(url)
            }
        }
        binding.stateBallot.setOnClickListener {
            val url = viewModel.ballotInfo.value
            if (url != null) {
                loadWebsite(url)
            }
        }

        viewModel.saved.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> {
                    binding.followUnfollowButton.apply {
                        text = getString(R.string.unfollow_election)
                        setOnClickListener {
                            viewModel.removeElection()
                        }
                    }
                }
                false -> {
                    binding.followUnfollowButton.apply {
                        text = getString(R.string.follow_election)
                        setOnClickListener {
                            viewModel.saveElection()
                        }
                    }
                }
            }
        })
        return binding.root
    }

    private fun loadWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}