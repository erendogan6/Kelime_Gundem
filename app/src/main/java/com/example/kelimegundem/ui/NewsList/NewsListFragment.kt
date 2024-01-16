package com.example.kelimegundem.ui.NewsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelimegundem.R
import com.example.kelimegundem.adaptor.NewsListAdaptor
import com.example.kelimegundem.adaptor.SearchHistoryAdaptor
import com.example.kelimegundem.databinding.FragmentNewsListBinding
import com.example.kelimegundem.model.Article
import com.example.kelimegundem.model.SearchHistoryItem
import com.example.kelimegundem.util.NewsItemListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsListFragment : Fragment(), NewsItemListener {
    private lateinit var binding: FragmentNewsListBinding
    val viewModel: NewsListViewModel by viewModels()
    private lateinit var newsAdapter: NewsListAdaptor
    private lateinit var searchAdapter: SearchHistoryAdaptor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentNewsListBinding.inflate(inflater,container,false)
        viewModel.setQuery("Android")
        viewModel.getSearchHistory()
        observeViewModel()
        setupRecyclerView()
        setupSearchView()
        setupSearchHistoryRecyclerView()
        return binding.root
    }
    override fun onNewsItemClicked(article: Article) {
        val action = NewsListFragmentDirections.actionNewsListFragmentToNewsDetailActivity2(article)
        findNavController().navigate(action)
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsListAdaptor(listOf(),this)
        binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = newsAdapter
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!viewModel.isLoading.value!! && totalItemCount <= (lastVisibleItem + 5)) {
                    viewModel.loadMoreNews()
                }
            }
        })
        }
    }

    private fun observeViewModel() {
        viewModel.newsItems.observe(viewLifecycleOwner) { newsList ->
            newsAdapter.updateNews(newsList)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.searchHistory.observe(viewLifecycleOwner) { history ->
            searchAdapter.updateHistory(history)
        }
    }

    private fun setupSearchView() {
        val searchEditTextId = resources.getIdentifier("android:id/search_src_text", null, null)
        val searchEditText = binding.searchView.findViewById<EditText>(searchEditTextId)
        searchEditText.setHintTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        searchEditText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.pt_serif_bold)
        searchEditText.typeface = typeface

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty() && it.isNotBlank()) {
                        viewModel.addSearchQueryToHistory(it)
                        viewModel.setQuery(it)
                        binding.searchView.queryHint = searchEditText.text
                        searchEditText.setText("")
                        binding.searchView.clearFocus()
                    }
                    else {
                        Toast.makeText(requireActivity(),"Lütfen Metin Giriniz",Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.recyclerViewSearchHistory.visibility = View.GONE
                return true
            }
        })

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (viewModel.searchHistory.value?.size!! >0) {
                    binding.recyclerViewSearchHistory.visibility = View.VISIBLE
                }
            } else {
                binding.recyclerViewSearchHistory.visibility = View.GONE
            }
        }
    }

    private fun onSearchHistoryItemClicked(query: String) {
        binding.searchView.setQuery(query, false)
        viewModel.setQuery(query)
        binding.searchView.queryHint = ""
        binding.searchView.clearFocus()
    }

    private fun onDeleteClicked(item: SearchHistoryItem) {
        viewModel.deleteSearchQuery(item)
    }

    private fun setupSearchHistoryRecyclerView() {
        searchAdapter = SearchHistoryAdaptor(listOf(),
            { query -> onSearchHistoryItemClicked(query) },
            { item -> onDeleteClicked(item) }
        )
        binding.recyclerViewSearchHistory.adapter = searchAdapter
        binding.recyclerViewSearchHistory.layoutManager = LinearLayoutManager(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchView.hasFocus()){
                    binding.searchView.clearFocus()
                }
                else if (binding.searchView.queryHint!! != "Arama Yapabilirsiniz"){
                    viewModel.setQuery("Android")
                    binding.searchView.setQuery("",false)
                    binding.searchView.queryHint = "Arama Yapabilirsiniz"
                }
                else{
                    requireActivity().finish()
                }
            }
        })
    }
}