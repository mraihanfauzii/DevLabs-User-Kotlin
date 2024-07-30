package com.hackathon.devlabsuser.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.home.VideoAdapter
import com.hackathon.devlabsuser.adapter.home.article.ArticleHomeAdapter
import com.hackathon.devlabsuser.adapter.home.PromoAdapter
import com.hackathon.devlabsuser.databinding.FragmentHomeBinding
import com.hackathon.devlabsuser.model.Article
import com.hackathon.devlabsuser.ui.ChatActivity
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.utils.ArticleDataDummy
import com.hackathon.devlabsuser.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthenticationManager
    private lateinit var articleAdapter: ArticleHomeAdapter
    private lateinit var promoAdapter: PromoAdapter
    private lateinit var homeViewModel: HomeViewModel
    private val videoIds = listOf(
        "CNPm1fkqIjc",
        "n_fKsJhYCeE",
        "fxCyKIM5jKY",
        "9TogelBk8KE",
        "AiUEElYSAok"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthenticationManager(requireContext())
        val getToken = authManager.getAccess(AuthenticationManager.TOKEN).toString()
        val token = "Bearer $getToken"

        articleAdapter = ArticleHomeAdapter()
        promoAdapter = PromoAdapter()
        promoAdapter.notifyDataSetChanged()
        articleAdapter.getArticles(ArticleDataDummy.listArticle)
        articleAdapter.setOnItemClickCallback(object: ArticleHomeAdapter.OnItemClickCallback {
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

        homeViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[HomeViewModel::class.java]
        homeViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            showLoading(loading)
        }
        homeViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        homeViewModel.getPromo(token)
        homeViewModel.promo.observe(viewLifecycleOwner) {
            if (it != null) {
                promoAdapter.getPromos(it)
            }
        }

        binding.tvArtikel.setOnClickListener {
            val intent = Intent(context, ArticleActivity::class.java)
            startActivity(intent)
        }

        binding.btnChat.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            startActivity(intent)
        }

        binding.btnDesainBangun.setOnClickListener {
            val dialog = QuestionnaireDialogFragment()
            dialog.show(childFragmentManager, "QuestionnaireDialogFragment")
        }

        binding.rvArtikel.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = articleAdapter
        }
        binding.rvPromo.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = promoAdapter
        }

        binding.rvTema.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = VideoAdapter(videoIds, viewLifecycleOwner.lifecycle)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}