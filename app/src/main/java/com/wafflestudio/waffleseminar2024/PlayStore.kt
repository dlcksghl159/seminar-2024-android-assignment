package com.wafflestudio.waffleseminar2024

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator

class PlayStore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_store)

        val mainLayout = findViewById<androidx.coordinatorlayout.widget.CoordinatorLayout>(R.id.main)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // Adapter 설정
        val adapter = MyFragmentStateAdapter(this)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val customTab = LayoutInflater.from(this).inflate(R.layout.tab_custom, null)

            val tabIcon = customTab.findViewById<ImageView>(R.id.tab_icon)
            val tabText = customTab.findViewById<TextView>(R.id.tab_text)

            when (position) {
                0 -> {
                    tabText.text = "게임"
                    tabIcon.setImageResource(R.drawable.ic_game)
                }
                1 -> {
                    tabText.text = "앱"
                    tabIcon.setImageResource(R.drawable.ic_app)
                }
                2 -> {
                    tabText.text = "검색"
                    tabIcon.setImageResource(R.drawable.ic_search)
                }
                3 -> {
                    tabText.text = "프로필"
                    tabIcon.setImageResource(R.drawable.ic_profile)
                }
            }

            // 탭에 커스텀 뷰 설정
            tab.customView = customTab
        }.attach()

// 탭 클릭 시 이미지 회전
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabView = tab?.customView
                val tabIcon = tabView?.findViewById<ImageView>(R.id.tab_icon)

                // 이미지 회전 애니메이션
                tabIcon?.animate()?.rotation(360f)?.setDuration(500)?.start()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabView = tab?.customView
                val tabIcon = tabView?.findViewById<ImageView>(R.id.tab_icon)

                // 선택 해제 시 회전 초기화
                tabIcon?.animate()?.rotation(-360f)?.setDuration(500)?.start()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 필요 시 재선택 로직 추가
            }
        })


        // Apply window insets to handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top, left = systemBars.left, right = systemBars.right, bottom = systemBars.bottom)
            insets
        }
    }
}