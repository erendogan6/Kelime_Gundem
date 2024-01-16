package com.example.kelimegundem.ui.NewsFavorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelimegundem.adaptor.NewsListAdaptor
import com.example.kelimegundem.databinding.FragmentNewsFavoritesBinding
import com.example.kelimegundem.model.Article
import com.example.kelimegundem.util.NewsItemListener

class NewsFavoritesFragment : Fragment(), NewsItemListener {
    private lateinit var viewModel: NewsFavoritesViewModel
    private lateinit var binding: FragmentNewsFavoritesBinding
    private lateinit var newsAdapter: NewsListAdaptor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(requireActivity())[NewsFavoritesViewModel::class.java]
        binding= FragmentNewsFavoritesBinding.inflate(inflater,container,false)
        setupRecyclerView()
        observeViewModel()
        viewModel.getFavorite()
        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsListAdaptor(listOf(),this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.newsFavoritesItems.observe(viewLifecycleOwner) { articles ->
            newsAdapter.updateNews(articles)
        }
    }

    override fun onNewsItemClicked(article: Article) {
        val action = NewsFavoritesFragmentDirections.actionNewsFavoritesFragmentToNewsDetailActivity(article)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorite()
    }
}