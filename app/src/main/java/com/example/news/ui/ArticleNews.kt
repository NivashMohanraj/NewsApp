package com.example.news.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.news.MainActivity
import com.example.news.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article_news.*

class ArticleNews : Fragment(R.layout.fragment_article_news) {
    lateinit var viewModel: NewsViewModel
    val args: ArticleNewsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = args.article
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view,"Article saved succesfully", Snackbar.LENGTH_SHORT).show()
        }

    }


}