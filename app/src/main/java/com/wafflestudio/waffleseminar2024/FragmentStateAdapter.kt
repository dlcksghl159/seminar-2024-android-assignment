package com.wafflestudio.waffleseminar2024;

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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

    private lateinit var searchView: SearchView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val historyList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        setupRecyclerView(view)
        setupSearchView(view)
        setupHistoryRecyclerView(view)
        return view
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
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
            // 장르 클릭 시 동작
        }
        recyclerView.adapter = adapter
    }

    private fun setupSearchView(view: View) {
        searchView = view.findViewById(R.id.search_view)
        loadSearchHistory()

        // 검색바 클릭 시 검색 기록 리사이클러뷰 보이기
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                historyRecyclerView.visibility = View.VISIBLE
            } else {
                historyRecyclerView.visibility = View.GONE
            }
        }

        // 검색어 입력 이벤트 처리
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        if (!historyList.contains(it)) {
                            historyList.add(0, it)
                            historyAdapter.notifyItemInserted(0)
                            saveSearchHistory()
                        }
                        // 검색 결과 처리
                        searchView.setQuery("", false)
                        searchView.clearFocus()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }


    private fun setupHistoryRecyclerView(view: View) {
        historyRecyclerView = view.findViewById(R.id.history_recycler_view)
        historyAdapter = HistoryAdapter(historyList) { selectedHistory ->
            // 검색 기록 아이템 클릭 시 동작
            searchView.setQuery(selectedHistory, true)
            historyRecyclerView.visibility = View.GONE
        }
        historyRecyclerView.layoutManager = LinearLayoutManager(context)
        historyRecyclerView.adapter = historyAdapter
    }

    // 검색 기록 저장
    private fun saveSearchHistory() {
        val sharedPreferences = requireContext().getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("history", historyList.toSet())
        editor.apply()
    }

    // 검색 기록 불러오기
    private fun loadSearchHistory() {
        val sharedPreferences = requireContext().getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
        val historySet = sharedPreferences.getStringSet("history", null)
        historySet?.let {
            historyList.clear()
            historyList.addAll(it)
        }
    }



    override fun onResume() {
        super.onResume()
        // 뒤로가기 버튼 콜백 등록
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (searchView.hasFocus()) {
                // 키보드가 열려 있으므로, 포커스를 제거하여 키보드를 닫습니다.
                searchView.clearFocus()
            } else if (historyRecyclerView.visibility == View.VISIBLE) {
                // 키보드는 닫혀 있고, 리사이클러뷰가 보이는 상태이므로 리사이클러뷰를 숨깁니다.
                historyRecyclerView.visibility = View.GONE
                searchView.onActionViewCollapsed()
            } else {
                // 뒤로가기 콜백 비활성화 후 기본 뒤로가기 동작 실행
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        // 프래그먼트가 일시 정지될 때 검색 기록 리사이클러뷰 숨기기
        historyRecyclerView.visibility = View.GONE
        searchView.clearFocus()
    }

}


class SettingsFragment : Fragment(R.layout.activity_user_information)