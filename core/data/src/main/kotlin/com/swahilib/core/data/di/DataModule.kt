
package com.swahilib.core.data.di

import com.swahilib.core.data.repository.DefaultRecentSearchRepository
import com.swahilib.core.data.repository.DefaultSearchContentsRepository
import com.swahilib.core.data.repository.NewsRepository
import com.swahilib.core.data.repository.OfflineFirstNewsRepository
import com.swahilib.core.data.repository.OfflineFirstTopicsRepository
import com.swahilib.core.data.repository.OfflineFirstUserDataRepository
import com.swahilib.core.data.repository.RecentSearchRepository
import com.swahilib.core.data.repository.SearchContentsRepository
import com.swahilib.core.data.repository.TopicsRepository
import com.swahilib.core.data.repository.UserDataRepository
import com.swahilib.core.data.util.ConnectivityManagerNetworkMonitor
import com.swahilib.core.data.util.NetworkMonitor
import com.swahilib.core.data.util.TimeZoneBroadcastMonitor
import com.swahilib.core.data.util.TimeZoneMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsTopicRepository(
        topicsRepository: OfflineFirstTopicsRepository,
    ): TopicsRepository

    @Binds
    internal abstract fun bindsNewsResourceRepository(
        newsRepository: OfflineFirstNewsRepository,
    ): NewsRepository

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository,
    ): UserDataRepository

    @Binds
    internal abstract fun bindsRecentSearchRepository(
        recentSearchRepository: DefaultRecentSearchRepository,
    ): RecentSearchRepository

    @Binds
    internal abstract fun bindsSearchContentsRepository(
        searchContentsRepository: DefaultSearchContentsRepository,
    ): SearchContentsRepository

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    internal abstract fun binds(impl: TimeZoneBroadcastMonitor): TimeZoneMonitor
}
