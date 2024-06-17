package com.capstone.tomguard.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.tomguard.databinding.FragmentProfileBinding
import com.capstone.tomguard.ui.MainViewModelFactory
import com.capstone.tomguard.ui.login.LoginActivity
import com.capstone.tomguard.data.Result

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<ProfileViewModel> {
        MainViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSession()
    }

    private fun getSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            } else {
                getProfile(user.token)
                Log.d("Debug", "getSession: ProfileFragment: token: ${user.token}")
            }
        }
    }

    private fun getProfile(token: String) {
        viewModel.getProfile(token).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                    Log.d("Debug", "getProfile: ProfileFragment: isLoading: true")
                }
                is Result.Success -> {
                    val user = it.data
                    showLoading(false)
                    binding.progressBar.visibility = View.VISIBLE
                    with(binding) {
                        tvUsername.text = user.name
                        tvEmail.text = user.email
                        Glide.with(this@ProfileFragment)
                            .load(user.profilePictureUrl)
                            .into(ivProfile)
                    Log.d("Debug", "getProfile: ProfileFragment: isSuccess: true")
                    }
                }
                is Result.Error -> showLoading(false)
                else -> {showLoading(false)}
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}