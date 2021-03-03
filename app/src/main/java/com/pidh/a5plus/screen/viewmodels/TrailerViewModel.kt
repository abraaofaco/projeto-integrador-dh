package com.pidh.a5plus.screen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pidh.a5plus.provider.IApiProvider
import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.provider.api.entities.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrailerViewModel @Inject constructor(
    private val appApi: IApiProvider
) : ViewModel() {

    private val mListTrailer = MutableLiveData<List<Video>>()
    val listTrailer: LiveData<List<Video>> = mListTrailer

    val selectTrailer = MutableLiveData<Video>()

    fun trailers(mediaType: String, id: Long) {
        viewModelScope.launch {
            val data = appApi.trailers(mediaType, id).videos.filter { video ->
                video.site == "YouTube"
            }

            mListTrailer.value = data
        }
    }
}