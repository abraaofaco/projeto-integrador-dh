package com.pidh.a5plus.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pidh.a5plus.R
import com.pidh.a5plus.provider.IApiProvider
import com.pidh.a5plus.provider.IAuthProvider
import com.pidh.a5plus.provider.api.tmdb.TMDBClient
import com.pidh.a5plus.provider.auth.FirebaseAuth
import com.pidh.a5plus.screen.adapter.CoverAdapter
import com.pidh.a5plus.screen.adapter.GenreAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun glideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .error(R.drawable.poster_not_available)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Singleton
    @Provides
    fun apiService(): IApiProvider = TMDBClient.instance

    @Singleton
    @Provides
    fun authService() = FirebaseAuth() as IAuthProvider
}