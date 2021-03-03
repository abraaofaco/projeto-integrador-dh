package com.pidh.a5plus.di

import com.bumptech.glide.RequestManager
import com.pidh.a5plus.screen.adapter.CoverAdapter
import com.pidh.a5plus.screen.adapter.GenreAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object HomeModule {

    @Provides
    fun coverAdapter(glide: RequestManager) = CoverAdapter(glide)

    @Provides
    fun genreAdapter(glide: RequestManager) = GenreAdapter(glide)

}