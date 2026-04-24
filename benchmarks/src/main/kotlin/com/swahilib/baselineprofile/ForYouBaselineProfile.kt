
package com.swahilib.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.swahilib.PACKAGE_NAME
import com.swahilib.foryou.forYouScrollFeedDownUp
import com.swahilib.foryou.forYouSelectTopics
import com.swahilib.foryou.forYouWaitForContent
import com.swahilib.startActivityAndAllowNotifications
import org.junit.Rule
import org.junit.Test

/**
 * Baseline Profile of the "For You" screen
 */
class ForYouBaselineProfile {
    @get:Rule val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() =
        baselineProfileRule.collect(PACKAGE_NAME) {
            startActivityAndAllowNotifications()

            // Scroll the feed critical user journey
            forYouWaitForContent()
            forYouSelectTopics(true)
            forYouScrollFeedDownUp()
        }
}
