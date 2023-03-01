package com.jae464.presentation.feed

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FeedStateAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FeedFolderFragment()
            1 -> return FeedCategoryFragment()
        }
        return FeedFolderFragment()
    }
}