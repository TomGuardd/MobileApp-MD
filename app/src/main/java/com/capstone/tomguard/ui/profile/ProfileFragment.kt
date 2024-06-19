@file:Suppress("DEPRECATION")

package com.capstone.tomguard.ui.profile

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.tomguard.R
import com.capstone.tomguard.databinding.FragmentProfileBinding
import com.capstone.tomguard.databinding.LayoutSettingLanguageBinding
import com.capstone.tomguard.ui.MainViewModelFactory
import com.capstone.tomguard.ui.login.LoginActivity
import com.capstone.tomguard.data.Result
import com.capstone.tomguard.data.model.ProfileResponse
import java.util.Locale

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var languageBinding: LayoutSettingLanguageBinding

    private val viewModel by viewModels<ProfileViewModel> {
        MainViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        languageBinding = LayoutSettingLanguageBinding.bind(binding.includedLanguageSetting.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            } else {
                getProfile(user.token)
            }
        }

        setupLanguageSpinner()
        binding.tvLogout.setOnClickListener { logout() }
    }

    private fun setupLanguageSpinner() {
        val languages = resources.getStringArray(R.array.language_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageBinding.spLanguage.adapter = adapter

        languageBinding.spLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setLocale("en")
                    1 -> setLocale("id")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setLocale(languageCode: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val currentLanguage = prefs.getString("language", "en")

        if (currentLanguage != languageCode) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)

            val editor = prefs.edit()
            editor.putString("language", languageCode)
            editor.apply()

            requireActivity().recreate()
        }
    }

    private fun getProfile(token: String) {
        viewModel.getProfile(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    val profile = result.data
                    setProfileData(profile)
                    showLoading(false)
                }
                is Result.Error -> {
                    showLoading(false)
                    binding.ivProfile.setImageResource(R.drawable.ic_account_circle_grey_128)
                    Log.e("ProfileFragment", "Error loading profile: ${result.error}")
                }
                null -> {
                    showLoading(false)
                    Log.d("ProfileFragment", "Result is null")
                }
            }
        }
    }

    private fun setProfileData(profile: ProfileResponse) {
        binding.apply {
            Glide.with(this@ProfileFragment)
                .load(profile.profilePictureUrl)
                .fitCenter()
                .into(ivProfile)
            tvUsername.text = profile.name
            tvEmail.text = profile.email
        }
    }

    private fun logout() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.logout_message))
            setPositiveButton(R.string.yes) { _, _ ->
                viewModel.logout()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            setNegativeButton(R.string.no) { _, _ -> }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.root.isEnabled = !isLoading
    }
}
