package com.hackathon.devlabsuser.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.home.VideoAdapter
import com.hackathon.devlabsuser.adapter.home.article.ArticleHomeAdapter
import com.hackathon.devlabsuser.adapter.home.PromoAdapter
import com.hackathon.devlabsuser.databinding.FragmentHomeBinding
import com.hackathon.devlabsuser.model.Article
import com.hackathon.devlabsuser.model.Promo
import com.hackathon.devlabsuser.ui.message.LastMessageActivity
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.ui.authentication.LoginActivity
import com.hackathon.devlabsuser.utils.ArticleDataDummy
import com.hackathon.devlabsuser.viewmodel.HomeViewModel
import com.hackathon.devlabsuser.viewmodel.QuestionnaireViewModel

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthenticationManager
    private lateinit var articleAdapter: ArticleHomeAdapter
    private lateinit var promoAdapter: PromoAdapter
    private lateinit var homeViewModel: HomeViewModel
    private val viewModel: QuestionnaireViewModel by viewModels({ requireParentFragment() })
    private val videoIds = listOf(
        "nwz1awsSTjI",
        "89cYlGvpAds",
        "c8IOB50ve7w",
        "qbn_dAVsrXM",
        "wSLFN_p_SZI"
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
        promoAdapter.setOnItemClickCallback(object: PromoAdapter.OnItemClickCallback {
            override fun onItemClicked(promo: Promo) {
                Intent(context, DetailPromoActivity::class.java).also {
                    it.putExtra(DetailPromoActivity.PHOTO_URL,promo.img)
                    it.putExtra(DetailPromoActivity.TITLE, promo.name)
                    it.putExtra(DetailPromoActivity.DESCRIPTION, "Perlu ditambah detail deskripsi promo di API-nya")
                    startActivity(it)
                }
            }
        })
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
            } else {
                startActivity(Intent(context, LoginActivity::class.java))
            }
        }

        binding.tvArtikel.setOnClickListener {
            val intent = Intent(context, ArticleActivity::class.java)
            startActivity(intent)
        }

        binding.btnChat.setOnClickListener {
            val intent = Intent(context, LastMessageActivity::class.java)
            startActivity(intent)
        }

        binding.btnDesainBangun.setOnClickListener {
            val dialog = QuestionnaireRecomDialogFragment()
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

    fun openQuestionnaire(questionIndex: Int) {
        viewModel.currentQuestion = questionIndex
        val dialogFragment = QuestionnaireDialogFragment()
        dialogFragment.show(childFragmentManager, "QuestionnaireDialogFragment")
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}