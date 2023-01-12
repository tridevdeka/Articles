package com.tridev.articles.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tridev.articles.R
import com.tridev.articles.databinding.FragmentArticlesBinding
import com.tridev.articles.model.Article
import com.tridev.articles.ui.adapter.ArticlesAdapter
import com.tridev.articles.utils.MarginDecoration
import com.tridev.articles.utils.MaterialMotionUtils.exitTransition
import com.tridev.articles.utils.Resource
import com.tridev.articles.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesFragment : Fragment(), ArticlesAdapter.ClickListener {

    private lateinit var mBinding: FragmentArticlesBinding
    private val viewModel: ArticleViewModel by activityViewModels()
    val TAG: String = this.javaClass.simpleName
    private lateinit var articlesAdapter: ArticlesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentArticlesBinding.bind(inflater.inflate(R.layout.fragment_articles,
            container,
            false))
        postponeEnterTransition()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpObserver()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            // Parent has been drawn. Start transitioning!
            startPostponedEnterTransition()
        }
    }

    private fun setUpRecyclerView() {
        articlesAdapter = ArticlesAdapter(requireActivity(), this)
        mBinding.rvArticles.apply {
            addItemDecoration(MarginDecoration(requireActivity(), R.dimen.grid_margin))
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = articlesAdapter
        }

    }

    private fun setUpObserver() {
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Error -> {
                    hideProgressBar()
                    Log.d(TAG, it.message.toString())
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { response ->
                        articlesAdapter.differ.submitList(response)
//                        Log.d(TAG, response.totalResults.toString())
                        Log.d(TAG,"SIZE:::"+ articlesAdapter.itemCount.toString())

                    }

                }
                is Resource.NetworkError -> {
                    Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "Something went wrong...", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun showProgressBar() {
        mBinding.progressBar.visibility = VISIBLE
    }

    private fun hideProgressBar() {
        mBinding.progressBar.visibility = GONE
    }

    private fun passData(article: Article, startView: View, transitionName: String) {
        viewModel.shareArticle(article)
        val extras = FragmentNavigatorExtras(startView to transitionName)
        val direction =
            ArticlesFragmentDirections.actionArticlesFragmentToArticleDetailsFragment(transitionName)
        findNavController().navigate(
            direction,
            extras)
    }

    override fun onClick(article: Article, startView: View, transitionName: String) {
        passData(article, startView, transitionName)
        exitTransition()
    }


}