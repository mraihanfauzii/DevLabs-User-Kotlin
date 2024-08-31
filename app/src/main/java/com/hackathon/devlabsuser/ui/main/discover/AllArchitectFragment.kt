package com.hackathon.devlabsuser.ui.main.discover

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.discover.AllArchitectAdapter
import com.hackathon.devlabsuser.databinding.FragmentAllArchitectBinding
import com.hackathon.devlabsuser.model.RecArchitect
import com.hackathon.devlabsuser.model.UserData
import com.hackathon.devlabsuser.ui.architect.ArchitectActivity
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.DiscoverViewModel

class AllArchitectFragment : Fragment() {
    private var _binding : FragmentAllArchitectBinding? = null
    private val binding get() = _binding!!
    private lateinit var architectAdapter: AllArchitectAdapter
    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var authManager: AuthenticationManager
    private var architectList: List<UserData> = emptyList()
    private var isAscending = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllArchitectBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        architectAdapter = AllArchitectAdapter(object: AllArchitectAdapter.OnItemClickCallback {
            override fun onItemClicked(architect: UserData) {
                Intent(context, ArchitectActivity::class.java).apply {
                    putExtra(ArchitectActivity.ID, architect.id)
                    putExtra(ArchitectActivity.NAME, architect.profileName)
                    putExtra(ArchitectActivity.DESCRIPTION, architect.profileDescription)
                    putExtra(ArchitectActivity.PHOTO_URL, architect.profilePicture)
                    putExtra(ArchitectActivity.PHONE_NUMBER, architect.phoneNumber)
                    putExtra(ArchitectActivity.EMAIL, architect.email)
                    putExtra(ArchitectActivity.ROLE, architect.role)
                    startActivity(this)
                }
            }
        })

        authManager = AuthenticationManager( requireContext())
        val getToken = authManager.getAccess(AuthenticationManager.TOKEN).toString()
        val token = "Bearer $getToken"

        discoverViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[DiscoverViewModel::class.java]
        discoverViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            showLoading(loading)
        }
        discoverViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        discoverViewModel.getAllArchitect(token)
        discoverViewModel.getAllArchitect.observe(viewLifecycleOwner) {
            if (it != null) {
                architectAdapter.getArchitects(it)
            }
            architectList = it
        }

        binding.apply {
            searchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filter(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            btnSort.setOnClickListener {
                isAscending = !isAscending
                sortArchitects(isAscending)
            }
            rvArsitek.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                adapter = architectAdapter
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun sortArchitects(isAscending: Boolean) {
        val sortedList = if (isAscending) {
            architectList.sortedBy { it.profileName }
        } else {
            architectList.sortedByDescending { it.profileName }
        }
        architectAdapter.getArchitects(sortedList)
    }

    private fun filter(text: String) {
        val filteredList = architectList.filter { it.profileName.contains(text, ignoreCase = true) }
        architectAdapter.getArchitects(filteredList)
    }
}