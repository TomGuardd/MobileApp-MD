package com.capstone.tomguard.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.tomguard.R
import com.capstone.tomguard.data.Result
import com.capstone.tomguard.data.datastore.SettingPreference
import com.capstone.tomguard.data.datastore.dataStore
import com.capstone.tomguard.data.model.ProfileResponse
import com.capstone.tomguard.databinding.FragmentProfileBinding
import com.capstone.tomguard.ui.MainViewModelFactory
import com.capstone.tomguard.ui.SettingsViewModelFactory
import com.capstone.tomguard.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val profileViewModel by viewModels<ProfileViewModel> {
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

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            } else {
                getProfile(user.token)
            }
        }

        val settingPref = SettingPreference.getInstance(requireContext().dataStore)

        val settingsViewModel = ViewModelProvider(
            requireActivity(),
            SettingsViewModelFactory(settingPref))[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.includedDarkmodeSetting.switch1.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.includedDarkmodeSetting.switch1.isChecked = false
            }
        }

        binding.includedDarkmodeSetting.switch1.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

        binding.tvLogout.setOnClickListener { logout() }
    }

    private fun getProfile(token: String) {
        profileViewModel.getProfile(token).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    val profile = it.data
                    setProfileData(profile)
                    showLoading(false)
                }

                is Result.Error -> {
                    showLoading(false)
                    binding.ivProfile.setImageResource(R.drawable.ic_account_circle_grey_128)
                }

                null -> showLoading(false)
            }
        }
    }

    private fun setProfileData(profile: ProfileResponse) {
        Log.d(
            "Debug",
            "setProfileData: ProfileFragment: profilePictureUrl: ${profile.profilePictureUrl}"
        )
        binding.apply {
            Glide
                .with(this@ProfileFragment)
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
                profileViewModel.logout()
            }
            setNegativeButton(R.string.no) { _, _ ->

            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}