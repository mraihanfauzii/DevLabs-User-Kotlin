package com.hackathon.devlabsuser.ui.architect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.architect.RatingAdapter
import com.hackathon.devlabsuser.databinding.FragmentArchitectReviewBinding
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.ArchitectViewModel

class ArchitectReviewFragment : Fragment() {
    private var _binding: FragmentArchitectReviewBinding? = null
    private val binding get() = _binding!!
    private val ratingViewModel: ArchitectViewModel by viewModels()
    private lateinit var ratingAdapter: RatingAdapter
    private lateinit var authenticationManager: AuthenticationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArchitectReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authenticationManager = AuthenticationManager(requireContext())

        ratingAdapter = RatingAdapter(emptyList())
        binding.rvRatings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRatings.adapter = ratingAdapter

        ratingViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        ratingViewModel.ratings.observe(viewLifecycleOwner, Observer { ratings ->
            ratingAdapter.updateRatings(ratings)
        })

        ratingViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        arguments?.getString("architect_id")?.let { architectId ->
            val getToken = authenticationManager.getAccess(AuthenticationManager.TOKEN).toString()
            val token = "Bearer $getToken"
            ratingViewModel.getRatings(token, architectId)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(architectId: String): ArchitectReviewFragment {
            val fragment = ArchitectReviewFragment()
            val args = Bundle().apply {
                putString("architect_id", architectId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
