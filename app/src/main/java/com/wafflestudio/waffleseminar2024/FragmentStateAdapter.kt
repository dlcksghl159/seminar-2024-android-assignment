package com.wafflestudio.waffleseminar2024;

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SearchFragment()
            2 -> ProfileFragment()
            3 -> SettingsFragment()
            else -> HomeFragment()
        }
    }

    override fun getItemCount(): Int {
        return 4 // 총 4개의 탭
    }
}

// 각 페이지에 대한 프래그먼트 정의 (예시)
class HomeFragment : Fragment(R.layout.fragment_game)
class SearchFragment : Fragment(R.layout.fragment_app)
class ProfileFragment : Fragment(R.layout.fragment_search)
class SettingsFragment : Fragment(R.layout.activity_user_information)