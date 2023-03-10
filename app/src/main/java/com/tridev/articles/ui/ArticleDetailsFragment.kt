package com.tridev.articles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.tridev.articles.databinding.FragmentArticleDetailsBinding
import com.tridev.articles.model.Article
import com.tridev.articles.utils.MaterialMotionUtils.applySharedElementTransition
import com.tridev.articles.viewmodel.ArticleViewModel


class ArticleDetailsFragment : Fragment() {

    private lateinit var mBinding: FragmentArticleDetailsBinding
    private val viewModel: ArticleViewModel by activityViewModels()
    private lateinit var article: Article
    private val args: ArticleDetailsFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applySharedElementTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentArticleDetailsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.root.transitionName = args.transitionName
        mBinding.progressBar.visibility = VISIBLE

        setUpObserver()
        setUpListener()
    }

    private fun setUpObserver() {
        viewModel.sharedArticle.observe(viewLifecycleOwner, Observer { article ->
            mBinding.webView.apply {
                webViewClient = WebViewClient()
                article.url?.let {
                    loadUrl(it)
                }

            }
            this.article = article
        })
    }

    private fun setUpListener() {
        mBinding.fbAdd.setOnClickListener {
            if (article.id != null) {
                viewModel.updateArticle(article.id!!)
                Toast.makeText(requireContext(), "Article Updated...", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertFavoriteArticle(article.copy(isFavorite = true))
                Toast.makeText(requireContext(), "Article Saved...", Toast.LENGTH_SHORT).show()
            }
        }
    }


    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mBinding.progressBar.visibility = GONE
        }
    }


}