package com.swahilib.core.database.di

import com.swahilib.core.database.AppDatabase
import com.swahilib.core.database.dao.NewsResourceDao
import com.swahilib.core.database.dao.NewsResourceFtsDao
import com.swahilib.core.database.dao.RecentSearchQueryDao
import com.swahilib.core.database.dao.TopicDao
import com.swahilib.core.database.dao.TopicFtsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesTopicsDao(
        database: AppDatabase,
    ): TopicDao = database.topicDao()

    @Provides
    fun providesNewsResourceDao(
        database: AppDatabase,
    ): NewsResourceDao = database.newsResourceDao()

    @Provides
    fun providesTopicFtsDao(
        database: AppDatabase,
    ): TopicFtsDao = database.topicFtsDao()

    @Provides
    fun providesNewsResourceFtsDao(
        database: AppDatabase,
    ): NewsResourceFtsDao = database.newsResourceFtsDao()

    @Provides
    fun providesRecentSearchQueryDao(
        database: AppDatabase,
    ): RecentSearchQueryDao = database.recentSearchQueryDao()
}
