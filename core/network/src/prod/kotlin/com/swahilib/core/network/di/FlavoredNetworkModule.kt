package com.swahilib.core.network.di

import com.swahilib.core.network.NiaNetworkDataSource
import com.swahilib.core.network.retrofit.RetrofitNiaNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun binds(impl: RetrofitNiaNetwork): NiaNetworkDataSource
}
