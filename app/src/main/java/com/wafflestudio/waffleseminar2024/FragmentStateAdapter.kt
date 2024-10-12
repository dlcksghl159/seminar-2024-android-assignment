package com.wafflestudio.waffleseminar2024;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GameFragment()
            1 -> AppFragment()
            2 -> SearchFragment()
            3 -> SettingsFragment()
            else -> GameFragment()
        }
    }

    override fun getItemCount(): Int {
        return 4 // 총 4개의 탭
    }
}

// 각 페이지에 대한 프래그먼트 정의 (예시)
class GameFragment : Fragment(R.layout.fragment_game)
class AppFragment : Fragment(R.layout.fragment_app)
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        setupRecyclerView(view)
        return view
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2) // 2개 컬럼 설정
        val genres = listOf(
            Genre(28, "액션", R.drawable.ic_action),
            Genre(12, "모험", R.drawable.ic_adventure),
            Genre(16, "애니메이션", R.drawable.ic_animation),
            Genre(35, "코미디", R.drawable.ic_comedy),
            Genre(80, "범죄", R.drawable.ic_crime),
            Genre(99, "다큐멘터리", R.drawable.ic_documentary),
            Genre(18, "드라마", R.drawable.ic_drama),
            Genre(10751, "가족", R.drawable.ic_family),
            Genre(14, "판타지", R.drawable.ic_fantasy),
            Genre(36, "역사", R.drawable.ic_history),
            Genre(27, "공포", R.drawable.ic_horror),
            Genre(10402, "음악", R.drawable.ic_music),
            Genre(9648, "미스터리", R.drawable.ic_mystery),
            Genre(10749, "로맨스", R.drawable.ic_romance),
            Genre(878, "SF", R.drawable.ic_scifi),
            Genre(10770, "TV 영화", R.drawable.ic_tv_movie),
            Genre(53, "스릴러", R.drawable.ic_thriller),
            Genre(10752, "전쟁", R.drawable.ic_war),
            Genre(37, "서부", R.drawable.ic_western)
        )

        adapter = GenreAdapter(genres) { genre ->
            //
        }
        recyclerView.adapter = adapter
    }

}

class SettingsFragment : Fragment(R.layout.activity_user_information)