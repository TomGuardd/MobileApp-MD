@file:Suppress("DEPRECATION")

package com.capstone.tomguard.ui.profile

import android.app.LocaleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
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
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            } else {
                getProfile(user.token)
            }
        }

        val settingPref = SettingPreference.getInstance(requireContext().dataStore)

        val settingsViewModel = ViewModelProvider(
            requireActivity(),
            SettingsViewModelFactory(settingPref))[SettingsViewModel::class.java]

        val language: Array<String> = resources.getStringArray(R.array.language_array)
        val languageArrayAdapter = ArrayAdapter(requireContext(), R.layout.layout_dropdown_language, language)

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.includedDarkmodeSetting.switch1.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.includedDarkmodeSetting.switch1.isChecked = false
            }
        }

        settingsViewModel.getLocaleSetting().observe(viewLifecycleOwner){
            if (it == "in"){
                binding.includedLanguageSetting.spLanguage.setSelection(languageArrayAdapter.getPosition(language[1]))
            } else {
                binding.includedLanguageSetting.spLanguage.setSelection(languageArrayAdapter.getPosition(language[0]))
            }
        }

        binding.includedLanguageSetting.spLanguage.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (parent?.getItemAtPosition(position).toString() == language[1]) {
                        setLocale("in", settingsViewModel)
                    } else {
                        setLocale("en", settingsViewModel)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            adapter = languageArrayAdapter
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
            Glide.with(this@ProfileFragment)
                .load(profile.profilePictureUrl)
                .fitCenter()
                .into(ivProfile)
            tvUsername.text = profile.name
            tvEmail.text = profile.email
        }
    }

    private fun setLocale(localeCode: String, settingViewModel: SettingsViewModel) {
        settingViewModel.saveLocaleSetting(localeCode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(localeCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(localeCode))
        }
    }

    private fun logout() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.logout_message))
            setPositiveButton(R.string.yes) { _, _ ->
                profileViewModel.logout()
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
