package com.hackathon.devlabsuser.ui.main.profile.favorite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.favorite.FavoriteArchitectAdapter
import com.hackathon.devlabsuser.databinding.FragmentFavoriteArchitectBinding
import com.hackathon.devlabsuser.model.Article
import com.hackathon.devlabsuser.model.UserData
import com.hackathon.devlabsuser.ui.architect.ArchitectActivity
import com.hackathon.devlabsuser.ui.main.home.DetailArticleActivity
import com.hackathon.devlabsuser.utils.ArticleDataDummy
import com.hackathon.devlabsuser.viewmodel.FavoriteViewModel

class FavoriteArchitectFragment : Fragment() {
    private var _binding : FragmentFavoriteArchitectBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteArchitectAdapter: FavoriteArchitectAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var architectList: List<UserData> = emptyList()
    private var isAscending = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteArchitectBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteArchitectAdapter = FavoriteArchitectAdapter()
        favoriteViewModel.allArchitectFavorites.observe(viewLifecycleOwner) { architect ->
            architectList = architect
            favoriteArchitectAdapter.getArchitects(architect)
        }
        binding.apply {
            rvArticle.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = favoriteArchitectAdapter
            }
            btnSort.setOnClickListener {
                isAscending = !isAscending
                sortArchitects(isAscending)
            }
            searchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filter(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
        favoriteArchitectAdapter.setOnItemClickCallback(object: FavoriteArchitectAdapter.OnItemClickCallback {
            override fun onItemClicked(architect: UserData) {
                Intent(context, ArchitectActivity::class.java).also {
                    it.putExtra(ArchitectActivity.ID, architect.id)
                    it.putExtra(ArchitectActivity.NAME, architect.profileName)
                    it.putExtra(ArchitectActivity.DESCRIPTION, architect.profileDescription)
                    it.putExtra(ArchitectActivity.PHOTO_URL, architect.profilePicture)
                    it.putExtra(ArchitectActivity.PHONE_NUMBER, architect.phoneNumber)
                    it.putExtra(ArchitectActivity.EMAIL, architect.email)
                    it.putExtra(ArchitectActivity.ROLE, architect.role)
                    startActivity(it)
                }
            }
        })
    }

    private fun sortArchitects(isAscending: Boolean) {
        val sortedList = if (isAscending) {
            architectList.sortedBy { it.profileName }
        } else {
            architectList.sortedByDescending { it.profileName }
        }
        favoriteArchitectAdapter.getArchitects(sortedList)
    }

    private fun filter(text: String) {
        val filteredList = architectList.filter { it.profileName.contains(text, ignoreCase = true) }
        favoriteArchitectAdapter.getArchitects(filteredList)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}