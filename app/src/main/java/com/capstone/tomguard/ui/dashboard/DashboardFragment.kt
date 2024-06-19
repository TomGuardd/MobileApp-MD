package com.capstone.tomguard.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.tomguard.databinding.FragmentDashboardBinding
import com.capstone.tomguard.ui.MainViewModelFactory
import com.capstone.tomguard.ui.main.MainViewModel
import com.capstone.tomguard.data.Result
import com.capstone.tomguard.ui.login.LoginActivity

class DashboardFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var binding: FragmentDashboardBinding

    private lateinit var adapter: HistoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPredictions.layoutManager = LinearLayoutManager(context)

        viewModel.getSession().observe(requireActivity()) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            } else {
                viewModel.getHistories(user.token)
            }
        }

        viewModel.historyList.observe(requireActivity()) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    showLoading(false)
                }

                is Result.Success -> {
                    showLoading(false)
                    adapter = HistoryListAdapter(it.data)
                    binding.rvPredictions.adapter = adapter
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}