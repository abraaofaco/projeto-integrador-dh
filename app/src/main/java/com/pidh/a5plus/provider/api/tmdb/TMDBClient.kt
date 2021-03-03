package com.pidh.a5plus.provider.api.tmdb

import com.pidh.a5plus.other.Constants.TMDB_API_URL
import com.pidh.a5plus.provider.IApiProvider
import com.pidh.a5plus.provider.api.tmdb.interceptor.TMDBInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TMDBClient {
    companion object {
        val instance: IApiProvider
            get() {
                val client = OkHttpClient()
                    .newBuilder()
                    .addInterceptor(TMDBInterceptor())
                    .build()

                val retrofit: Retrofit =
                    Retrofit.Builder().baseUrl(TMDB_API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()

                return retrofit.create(TMDBRepository::class.java)
            }
    }
}