package com.wafflestudio.waffleseminar2024;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.assignment0.Movie
import com.example.assignment0.MovieAdapter
import com.example.assignment0.MovieResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.IOException

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
    private lateinit var movieAdapter: MovieAdapter
    private val movieList = mutableListOf<Movie>()
    private val filteredList = mutableListOf<Movie>()
    private val genreMap = hashMapOf<String, Int>()
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var searchBar: SearchView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val historyList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initializeSearchBar(view)  // searchBar를 먼저 초기화
        setupRecyclerView(view)
        setupHistoryRecyclerView(view)  // 이후에 setupHistoryRecyclerView 호출
        initializeRecyclerView(view)
        initializeBackButtonHandling()

        loadSearchHistory()
        loadGenres()
        loadMovies()
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

    private fun initializeSearchBar(view: View) {
        val searchBar: EditText = view.findViewById(R.id.searchBar)
        searchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showHistoryRecyclerView()  // 검색바에 포커스가 갈 때 검색 기록 표시
                historyRecyclerView.scrollToPosition(0)
            } else {
                hideHistoryRecyclerView()  // 포커스를 잃으면 검색 기록 숨기기
            }
        }
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handleSearchAction(searchBar.text.toString().trim())
                hideHistoryRecyclerView()
                true
            } else {
                false
            }
        }
    }

    private fun initializeBackButtonHandling() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (historyRecyclerView.visibility == View.VISIBLE) {
                hideHistoryRecyclerView()  // 검색 기록이 보일 때 뒤로 가기 누르면 기록 숨기기
            } else if (movieRecyclerView.visibility == View.VISIBLE) {
                movieRecyclerView.visibility = View.GONE  // 뒤로 가기 시 리사이클러뷰 숨기기
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    private fun handleSearchAction(query: String) {
        Log.d("SearchFragment", "Search query: $query")
        filteredList.clear()

        if (query.isNotEmpty()) {
            movieRecyclerView.visibility = View.VISIBLE
            if (genreMap.containsKey(query)) {
                filterMoviesByGenre(query)
            } else {
                filterMoviesByTitle(query)
            }

            // 검색 기록에 추가 및 저장
            if (!historyList.contains(query)) {
                historyList.add(0, query)  // 최신 기록을 상단에 추가
                saveSearchHistory()
            }

        } else {
            movieRecyclerView.visibility = View.VISIBLE
            filteredList.addAll(movieList)  // 모든 영화 표시
        }
        movieAdapter.notifyDataSetChanged()
        hideKeyboard()
    }

    private fun setupHistoryRecyclerView(view: View) {
        historyRecyclerView = view.findViewById(R.id.history_recycler_view)
        historyAdapter = HistoryAdapter(historyList) { selectedHistory ->
            handleSearchAction(selectedHistory)  // 기록을 검색어로 사용해 검색 수행
            hideHistoryRecyclerView()  // 검색 기록 숨기기
        }
        historyRecyclerView.layoutManager = LinearLayoutManager(context)
        historyRecyclerView.adapter = historyAdapter
    }

    // 검색 기록 저장
    private fun saveSearchHistory() {
        val sharedPreferences =
            requireContext().getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("history", historyList.toSet())
        editor.apply()
    }

    // 검색 기록 불러오기
    private fun loadSearchHistory() {
        val sharedPreferences =
            requireContext().getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
        val historySet = sharedPreferences.getStringSet("history", null)
        historySet?.let {
            historyList.clear()
            historyList.addAll(it)
        }
    }

    private fun loadGenres() {
        genreMap["액션"] = 28
        genreMap["모험"] = 12
        genreMap["애니메이션"] = 16
        genreMap["코미디"] = 35
        genreMap["범죄"] = 80
        genreMap["다큐멘터리"] = 99
        genreMap["드라마"] = 18
        genreMap["가족"] = 10751
        genreMap["판타지"] = 14
        genreMap["역사"] = 36
        genreMap["공포"] = 27
        genreMap["음악"] = 10402
        genreMap["미스터리"] = 9648
        genreMap["로맨스"] = 10749
        genreMap["SF"] = 878
        genreMap["TV 영화"] = 10770
        genreMap["스릴러"] = 53
        genreMap["전쟁"] = 10752
        genreMap["서부"] = 37
    }

    private fun loadMovies() {
        val jsonString = loadJSONFromAsset("movies.json")

        if (jsonString != null) {
            parseMovies(jsonString)
        } else {
            Log.e("SearchFragment", "Failed to load JSON data.")
        }
    }

    private fun loadJSONFromAsset(fileName: String): String? {
        return try {
            val inputStream = context?.assets?.open(fileName)
            val size = inputStream?.available() ?: 0
            val buffer = ByteArray(size)
            inputStream?.read(buffer)
            inputStream?.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    private fun parseMovies(jsonString: String) {
        try {
            val gson = Gson()
            val movieResponse = gson.fromJson(jsonString, MovieResponse::class.java)
            movieList.addAll(movieResponse.movies)
            Log.d("SearchFragment", "Movies loaded: ${movieList.size}")
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
    }

    private fun filterMoviesByGenre(genre: String) {
        val genreId = genreMap[genre] ?: return
        for (movie in movieList) {
            if (movie.genreIds?.contains(genreId) == true) {
                filteredList.add(movie)
            }
        }
        movieAdapter.notifyDataSetChanged()
    }

    private fun filterMoviesByTitle(title: String) {
        for (movie in movieList) {
            if (movie.title.contains(title, ignoreCase = true)) {
                filteredList.add(movie)
            }
        }
        movieAdapter.notifyDataSetChanged()
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun initializeRecyclerView(view: View) {
        movieRecyclerView = view.findViewById(R.id.movieRecyclerView)
        movieAdapter = MovieAdapter(filteredList)
        movieRecyclerView.layoutManager = GridLayoutManager(context, 3)
        movieRecyclerView.adapter = movieAdapter
        movieRecyclerView.visibility = View.GONE  // 초기에는 리사이클러뷰 숨기기
    }

    private fun showHistoryRecyclerView() {
        historyRecyclerView.visibility = View.VISIBLE
    }

    private fun hideHistoryRecyclerView() {
        historyRecyclerView.visibility = View.GONE
    }



}

class SettingsFragment : Fragment(R.layout.activity_user_information)