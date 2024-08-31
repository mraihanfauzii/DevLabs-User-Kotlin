package com.hackathon.devlabsuser.ui.architect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.adapter.architect.ArchitectFragmentAdapter
import com.hackathon.devlabsuser.databinding.ActivityArchitectBinding
import com.hackathon.devlabsuser.model.UserData
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.ui.main.home.QuestionnaireDialogFragment
import com.hackathon.devlabsuser.viewmodel.ArchitectViewModel
import com.hackathon.devlabsuser.viewmodel.FavoriteViewModel
import com.hackathon.devlabsuser.viewmodel.QuestionnaireViewModel

class ArchitectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArchitectBinding
    private lateinit var authenticationManager: AuthenticationManager
    private val architectViewModel: ArchitectViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var viewPagerAdapter: ArchitectFragmentAdapter
    private val viewModel: QuestionnaireViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchitectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationManager = AuthenticationManager(this)

        val bundle = Bundle()
        val id = intent.getStringExtra(ID)
        val profilePicture = intent.getStringExtra(PHOTO_URL)
        val profileName = intent.getStringExtra(NAME)
        val email = intent.getStringExtra(EMAIL)
        val phoneNumber = intent.getStringExtra(PHONE_NUMBER)
        val profileDescription = intent.getStringExtra(DESCRIPTION)
        val role = intent.getStringExtra(ROLE)

        bundle.putString("architect_id", id)

        val getToken = authenticationManager.getAccess(AuthenticationManager.TOKEN).toString()
        val token = "Bearer $getToken"

        architectViewModel.ratingsAverage.observe(this, Observer { ratingsAverage ->
            binding.tvUserRatingsAverage.text = "Average Rating : "+ratingsAverage.averageRating
        })

        architectViewModel.getRatingsAverage(token, id!!)

        viewPagerAdapter = ArchitectFragmentAdapter(supportFragmentManager, lifecycle, bundle)
        with(binding) {
            ViewPager.adapter = viewPagerAdapter
            TabLayoutMediator(TabLayout, ViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Portfolio"
                    1 -> tab.text = "Ulasan"
                }
            }.attach()
        }

        binding.apply {
            fabPesan.setOnClickListener {
                val questionnaireFragment = QuestionnaireDialogFragment()
                val bundle = Bundle()
                bundle.putString("architect_id", id) // 'id' adalah architect_id yang didapat dari Intent
                questionnaireFragment.arguments = bundle
                questionnaireFragment.show(supportFragmentManager, "QuestionnaireDialogFragment")
            }
            tvUserName.text = profileName ?: "Unknown"
            tvDescription.text = profileDescription
            tvNoTelepon.text = phoneNumber
            Glide.with(this@ArchitectActivity)
                .load("https://www.bumpy-insects-reply-yearly.a276.dcdg.xyz" + profilePicture)
                .centerCrop()
                .placeholder(R.drawable.contoh_profile)
                .into(ivUserProfile)

            favoriteViewModel.allArchitectFavorites.observe(this@ArchitectActivity) { favorites ->
                val isFavorited = favorites.any { it.id == id }
                toggleFavorite.isChecked = isFavorited
                toggleFavorite.setBackgroundResource(if (isFavorited) R.drawable.ic_favorite_after else R.drawable.ic_favorite_before)
            }

            toggleFavorite.setOnCheckedChangeListener { _, isChecked ->
                val architect = UserData(id,email ?: "", phoneNumber ?: "", profileName ?: "", profilePicture ?: "", profileDescription ?: "", role ?: "",  "")
                if (isChecked) {
                    toggleFavorite.setBackgroundResource(R.drawable.ic_favorite_after)
                    favoriteViewModel.insertArchitectFavorite(architect)
                } else {
                    toggleFavorite.setBackgroundResource(R.drawable.ic_favorite_before)
                    favoriteViewModel.deleteArchitectFavorite(architect)
                }
            }
        }
    }

    fun openQuestionnaire(questionIndex: Int) {
        viewModel.currentQuestion = questionIndex
        val dialogFragment = QuestionnaireDialogFragment()
        dialogFragment.show(supportFragmentManager, "QuestionnaireDialogFragment")
    }

    companion object {
        const val ID = "id"
        const val PHOTO_URL = "photo_url"
        const val NAME = "name"
        const val EMAIL = "email"
        const val PHONE_NUMBER = "phone_number"
        const val DESCRIPTION = "description"
        const val ROLE = "role"
    }
}