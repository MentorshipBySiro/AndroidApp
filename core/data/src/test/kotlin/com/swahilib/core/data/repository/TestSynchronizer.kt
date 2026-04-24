
package com.swahilib.core.data.repository

import com.swahilib.core.data.Synchronizer
import com.swahilib.core.datastore.ChangeListVersions
import com.swahilib.core.datastore.NiaPreferencesDataSource

/**
 * Test synchronizer that delegates to [NiaPreferencesDataSource]
 */
class TestSynchronizer(
    private val niaPreferences: NiaPreferencesDataSource,
) : Synchronizer {
    override suspend fun getChangeListVersions(): ChangeListVersions =
        niaPreferences.getChangeListVersions()

    override suspend fun updateChangeListVersions(
        update: ChangeListVersions.() -> ChangeListVersions,
    ) = niaPreferences.updateChangeListVersion(update)
}
