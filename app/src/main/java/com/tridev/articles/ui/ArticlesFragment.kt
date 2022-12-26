package com.tridev.articles.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tridev.articles.R
import com.tridev.articles.databinding.FragmentArticlesBinding
import com.tridev.articles.utils.Resource
import com.tridev.articles.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesFragment : Fragment() {

    private lateinit var mBinding: FragmentArticlesBinding
    private val viewModel: ArticleViewModel by viewModels()
    val TAG: String = this.javaClass.simpleName


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentArticlesBinding.bind(inflater.inflate(R.layout.fragment_articles,
            container,
            false))
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Error -> {
                    Log.d(TAG, it.message.toString())
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    it.data?.let {response->
                        Log.d(TAG, response.totalResults.toString())
                    }
                }
            }
        })
    }
}