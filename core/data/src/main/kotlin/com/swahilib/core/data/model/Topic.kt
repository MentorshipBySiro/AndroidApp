package com.swahilib.core.data.model

import com.swahilib.core.database.model.TopicEntity
import com.swahilib.core.network.model.NetworkTopic

fun NetworkTopic.asEntity() = TopicEntity(
    id = id,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    url = url,
    imageUrl = imageUrl,
)
