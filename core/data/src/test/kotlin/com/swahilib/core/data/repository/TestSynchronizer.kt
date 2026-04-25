package com.swahilib.core.data.repository

import com.swahilib.core.data.Synchronizer
import com.swahilib.core.datastore.ChangeListVersions
import com.swahilib.core.datastore.AppPreferencesDataSource

/**
 * Test synchronizer that delegates to [AppPreferencesDataSource]
 */
class TestSynchronizer(
    private val appPreferences: AppPreferencesDataSource,
) : Synchronizer {
    override suspend fun getChangeListVersions(): ChangeListVersions =
        appPreferences.getChangeListVersions()

    override suspend fun updateChangeListVersions(
        update: ChangeListVersions.() -> ChangeListVersions,
    ) = appPreferences.updateChangeListVersion(update)
}
