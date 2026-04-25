package com.swahilib.feature.search.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.swahilib.core.navigation.Navigator
import com.swahilib.feature.interests.api.navigation.InterestsNavKey
import com.swahilib.feature.search.api.navigation.SearchNavKey
import com.swahilib.feature.search.impl.SearchScreen
import com.swahilib.feature.topic.api.navigation.navigateToTopic

fun EntryProviderScope<NavKey>.searchEntry(navigator: Navigator) {
    entry<SearchNavKey> {
        SearchScreen(
            onBackClick = { navigator.goBack() },
            onInterestsClick = { navigator.navigate(InterestsNavKey()) },
            onTopicClick = navigator::navigateToTopic,
        )
    }
}
