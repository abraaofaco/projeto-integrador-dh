package com.pidh.a5plus.provider.api.tmdb.interceptor

import android.util.Log
import com.pidh.a5plus.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class TMDBInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDBApiKey)
            .addQueryParameter("language", "pt-BR")
            .build()

        Log.i("@@", url.toString())

        val requestBuilder = original.newBuilder()
            .url(url)
            .build()

        return chain.proceed(requestBuilder)
    }
}