package com.swahilib.core.network

import com.swahilib.core.network.model.NetworkChangeList
import com.swahilib.core.network.model.NetworkNewsResource
import com.swahilib.core.network.model.NetworkTopic

/**
 * Interface representing network calls to the App backend
 */
interface NiaNetworkDataSource {
    suspend fun getTopics(ids: List<String>? = null): List<NetworkTopic>

    suspend fun getNewsResources(ids: List<String>? = null): List<NetworkNewsResource>

    suspend fun getTopicChangeList(after: Int? = null): List<NetworkChangeList>

    suspend fun getNewsResourceChangeList(after: Int? = null): List<NetworkChangeList>
}
