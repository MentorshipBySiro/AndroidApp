package com.swahilib.feature.foryou.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.swahilib.core.navigation.Navigator
import com.swahilib.feature.foryou.api.navigation.ForYouNavKey
import com.swahilib.feature.foryou.impl.ForYouScreen
import com.swahilib.feature.topic.api.navigation.navigateToTopic

fun EntryProviderScope<NavKey>.forYouEntry(navigator: Navigator) {
    entry<ForYouNavKey> {
        ForYouScreen(
            onTopicClick = navigator::navigateToTopic,
        )
    }
}
