package com.jae464.presentation.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.databinding.FragmentSettingsBinding
import com.jae464.presentation.R

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppBar()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.settingToolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.settingToolbar.setOnMenuItemClickListener {
            Log.d("MyPageFragment", it.toString())
            when (it.itemId) {
                R.id.setting -> {
                    findNavController().navigate(R.id.settings)
                }
            }
            true
        }

    }
}