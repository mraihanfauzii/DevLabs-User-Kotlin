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
import com.hackathon.devlabsuser.adapter.favorite.FavoriteArticleAdapter
import com.hackathon.devlabsuser.databinding.FragmentFavoriteArticleBinding
import com.hackathon.devlabsuser.model.Article
import com.hackathon.devlabsuser.ui.main.home.DetailArticleActivity
import com.hackathon.devlabsuser.viewmodel.FavoriteViewModel

class FavoriteArticleFragment : Fragment() {
    private var _binding : FragmentFavoriteArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteArticleAdapter: FavoriteArticleAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var articleList: List<Article> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteArticleBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteArticleAdapter = FavoriteArticleAdapter()
        favoriteViewModel.allArticleFavorites.observe(viewLifecycleOwner) { articles ->
            articleList = articles
            favoriteArticleAdapter.getArticles(articles)
        }
        binding.rvArticle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = favoriteArticleAdapter
        }
        favoriteArticleAdapter.setOnItemClickCallback(object: FavoriteArticleAdapter.OnItemClickCallback {
            override fun onItemClicked(article: Article) {
                Intent(context, DetailArticleActivity::class.java).also {
                    it.putExtra(DetailArticleActivity.PHOTO_URL, article.photoUrl)
                    it.putExtra(DetailArticleActivity.TITLE, article.title)
                    it.putExtra(DetailArticleActivity.DESCRIPTION, article.description)
                    it.putExtra(DetailArticleActivity.ID, article.id.toString())
                    startActivity(it)
                }
            }
        })

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filter(text: String) {
        val filteredList = articleList.filter { it.title.contains(text, ignoreCase = true) }
        favoriteArticleAdapter.getArticles(filteredList)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}