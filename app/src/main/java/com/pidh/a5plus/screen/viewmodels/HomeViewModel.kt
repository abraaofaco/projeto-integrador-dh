package com.pidh.a5plus.screen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pidh.a5plus.helper.Util.addDays
import com.pidh.a5plus.helper.Util.addMonths
import com.pidh.a5plus.helper.Util.dateToString
import com.pidh.a5plus.other.enum.MediaType
import com.pidh.a5plus.provider.IApiProvider
import com.pidh.a5plus.provider.api.entities.Casting
import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.provider.api.model.GenreItemsModel
import com.pidh.a5plus.provider.database.local.RoomRepository
import com.pidh.a5plus.provider.database.local.entities.Genre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

import java.util.*

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRoom: RoomRepository,
    private val appApi: IApiProvider
) : ViewModel() {

    private var reload = false

    var genreListMovie: List<Genre> = listOf()
        private set

    var genreListTvSeries: List<Genre> = listOf()
        private set

    private val mListPopularMovies = MutableLiveData<List<GenreItemsModel>>()
    val listPopularMovies: LiveData<List<GenreItemsModel>> = mListPopularMovies

    private val mListPopularTvSeries = MutableLiveData<List<GenreItemsModel>>()
    val listPopularTvSeries: LiveData<List<GenreItemsModel>> = mListPopularTvSeries

    private val mListInTheaters = MutableLiveData<List<GenreItemsModel>>()
    val listInTheaters: LiveData<List<GenreItemsModel>> = mListInTheaters

    private val mListUpcomingReleases = MutableLiveData<List<GenreItemsModel>>()
    val listUpcomingReleases: LiveData<List<GenreItemsModel>> = mListUpcomingReleases

    private val mListOfSearchResult = MutableLiveData<Set<Movie>>()
    val listOfSearchResult: LiveData<Set<Movie>> = mListOfSearchResult

    private val mListCasting = MutableLiveData<List<Casting>>()
    val listCasting: LiveData<List<Casting>> = mListCasting

    val selectMovie = MutableLiveData<Movie>()

    init {
        loadGenres()
    }

    fun findAndSelect(mediaType: String, codeMovie: Long) {
        viewModelScope.launch {
            val movie = appApi.find(mediaType, codeMovie)
            movie.mediaType = mediaType

            selectMovie.value = movie
        }
    }

    fun popularMovies() {
        viewModelScope.launch {
            val data: MutableList<GenreItemsModel> = mutableListOf()
            genreListMovie.filter { it.enable }.forEach { genre ->
                val movies = appApi.mostPopular(genre.mediaType, genre.codeMovie).movies

                movies.forEach {
                    it.mediaType = genre.mediaType
                }

                if (movies.isNotEmpty()) {
                    data.add(GenreItemsModel(genre.id, genre.name, movies))
                }
            }

            mListPopularMovies.value = data
        }
    }

    fun popularTvSeries() {
        viewModelScope.launch {
            val data: MutableList<GenreItemsModel> = mutableListOf()
            genreListTvSeries.filter { it.enable }.forEach { genre ->
                val movies = appApi.mostPopular(genre.mediaType, genre.codeMovie).movies

                movies.forEach {
                    it.mediaType = genre.mediaType
                }

                if (movies.isNotEmpty()) {
                    data.add(GenreItemsModel(genre.id, genre.name, movies))
                }
            }

            mListPopularTvSeries.value = data
        }
    }

    fun nowPlayingInTheaters() {
        viewModelScope.launch {
            val minimum = Date().addMonths(-2).dateToString("yyyy-MM-dd")
            val maximum = Date().dateToString("yyyy-MM-dd")

            val data: MutableList<GenreItemsModel> = mutableListOf()
            genreListMovie.filter { it.enable }.forEach { genre ->
                val movies = appApi.inTheaters(minimum, maximum, genre.codeMovie).movies

                movies.forEach {
                    it.mediaType = genre.mediaType
                }

                if (movies.isNotEmpty()) {
                    data.add(GenreItemsModel(genre.id, genre.name, movies))
                }
            }

            mListInTheaters.value = data
        }
    }

    fun upcomingReleases() {
        viewModelScope.launch {
            val minimum = Date().addDays(1).dateToString("yyyy-MM-dd")
            val maximum = Date().addMonths(1).dateToString("yyyy-MM-dd")

            val data: MutableList<GenreItemsModel> = mutableListOf()
            genreListMovie.filter { it.enable }.forEach { genre ->
                val movies = appApi.inTheaters(minimum, maximum, genre.codeMovie).movies

                movies.forEach {
                    it.mediaType = genre.mediaType
                }

                if (movies.isNotEmpty()) {
                    data.add(GenreItemsModel(genre.id, genre.name, movies))
                }
            }

            mListUpcomingReleases.value = data
        }
    }

    fun searchMovies(query: String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                mListOfSearchResult.value = appApi.searchMovies(query).movies
                    .filter {
                        !it.posterPath.isNullOrBlank() && it.mediaType != MediaType.PERSON.code
                    }.sortedByDescending {
                        it.voteAverage
                    }.toSet()
            }
        }
    }

    fun castingDetail(mediaType: String, id: Long) {
        viewModelScope.launch {
            val data = appApi.castDetail(mediaType, id).listCast.sortedBy { it.order }

            mListCasting.value = data
        }
    }

    fun reload() {
        if (reload) {
            viewModelScope.launch {
                loadGenres()
                reload = false

                //Evitar load de abas que ainda não foram abertas
                if (mListPopularMovies.value != null) popularMovies()
                if (mListPopularTvSeries.value != null) popularTvSeries()
                if (mListInTheaters.value != null) nowPlayingInTheaters()
                if (mListUpcomingReleases.value != null) upcomingReleases()
            }
        }
    }

    fun updateGenre(genre: Genre) {
        viewModelScope.launch {
            appRoom.genreDao().update(genre)
            reload = true
        }
    }

    private fun loadGenres() {
        val dao = appRoom.genreDao()

        genreListMovie = dao.get("movie")
        genreListTvSeries = dao.get("tv")
    }


}