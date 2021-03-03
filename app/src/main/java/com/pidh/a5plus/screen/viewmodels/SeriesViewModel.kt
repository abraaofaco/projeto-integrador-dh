package com.pidh.a5plus.screen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pidh.a5plus.provider.IApiProvider
import com.pidh.a5plus.provider.api.entities.SeriesInfo
import com.pidh.a5plus.provider.api.response.EpisodesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val appApi: IApiProvider
) : ViewModel() {

    private val mListSeason = MutableLiveData<List<SeriesInfo>>()
    val listSeason: LiveData<List<SeriesInfo>> = mListSeason

    private val mListEpisodes = MutableLiveData<EpisodesResponse>()
    val episodesResponse: LiveData<EpisodesResponse> = mListEpisodes

    fun seasons(id: Long) {
        viewModelScope.launch {
            val data = appApi.seasons(id).listSeasons.sortedBy {
                it.seasonNumber
            }
            mListSeason.value = data
        }
    }

    fun episodes(seriesId: Long, seasonNumber: Int) {
        viewModelScope.launch {
            mListEpisodes.value = appApi.episodes(seriesId, seasonNumber)
        }
    }
}