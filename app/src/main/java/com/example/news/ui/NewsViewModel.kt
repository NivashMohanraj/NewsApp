package com.example.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.repository.NewsRepository
import com.example.news.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel (

    val newsRepository: NewsRepository
    ): ViewModel() {

        val breakingNews : MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
        var breakingNewsPage = 1
        var breakingNewsResponse: NewsResponse? = null


        val searchNews : MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
        var searchNewsPage = 1
        var searchNewsResponse: NewsResponse? = null

        init {

            getBreakingNews("us")
        }

        fun getBreakingNews(countryCode: String) = viewModelScope.launch {
            breakingNews.postValue(Resources.Loading())
            val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))
        }

        fun searchNews(searchQuery: String) = viewModelScope.launch {
            searchNews.postValue(Resources.Loading())
            val response = newsRepository.searchNews(searchQuery, searchNewsPage)
            searchNews.postValue(handleSearchNewsResponse(response))
        }

        private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse>? {
            if (response.isSuccessful){
                response.body()?.let { resultResponse ->
                    breakingNewsPage++
                    if (breakingNewsResponse == null){
                        breakingNewsResponse = resultResponse
                    }else{
                        val oldArticles = breakingNewsResponse?.articles
                        val newArticles = resultResponse.articles
                        oldArticles?.addAll(newArticles)
                    }
                    return Resources.Success(breakingNewsResponse?: resultResponse)
                }
            }
            return Resources.Error(response.message())
        }

        private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse>? {
            if (response.isSuccessful){
                response.body()?.let { resultResponse ->
                    searchNewsPage++
                    if (searchNewsResponse == null){
                        searchNewsResponse = resultResponse
                    }else{
                        val oldArticles = searchNewsResponse?.articles
                        val newArticles = resultResponse.articles
                        oldArticles?.addAll(newArticles)
                    }
                    return Resources.Success(searchNewsResponse?: resultResponse)
                }
            }
            return Resources.Error(response.message())
        }

        fun saveArticle(article: Article) = viewModelScope.launch {
            newsRepository.upsert(article)
        }

        fun getSavedNews() = newsRepository.getSavedNews()

        fun deleteArticle(article: Article) = viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
}