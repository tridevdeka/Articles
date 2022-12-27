package com.tridev.articles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tridev.articles.R
import com.tridev.articles.databinding.FragmentArticleSavedBinding
import com.tridev.articles.model.Article
import com.tridev.articles.ui.adapter.ArticlesAdapter
import com.tridev.articles.utils.MarginDecoration
import com.tridev.articles.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleSavedFragment : Fragment(), ArticlesAdapter.ClickListener,
    ArticlesAdapter.DeleteListener {
    private val viewModel: ArticleViewModel by activityViewModels()
    private lateinit var mBinding: FragmentArticleSavedBinding
    private lateinit var articleAdapter: ArticlesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentArticleSavedBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setUpObserver()
    }

    private fun passData(article: Article) {
        viewModel.shareArticle(article)
        findNavController().navigate(R.id.action_articleSavedFragment_to_articleDetailsFragment)
    }


    private fun setUpObserver() {
        viewModel.getSavedArticles().observe(viewLifecycleOwner, Observer {

            articleAdapter.differ.submitList(it)

            if (it.isEmpty()) {
                mBinding.tvEmptyArticle.visibility = VISIBLE
            } else {
                mBinding.tvEmptyArticle.visibility = GONE
            }
        })
    }


    private fun setupRecyclerView() {
        articleAdapter = ArticlesAdapter(requireActivity(), this, this, isSavedArticle = true)
        mBinding.rvSavedArticle.apply {
            addItemDecoration(MarginDecoration(requireActivity(), R.dimen.grid_margin))
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = articleAdapter
        }
    }

    override fun onClick(article: Article) {
        passData(article)
    }

    override fun onDelete(article: Article) {
        viewModel.deleteArticle(article)
    }

}