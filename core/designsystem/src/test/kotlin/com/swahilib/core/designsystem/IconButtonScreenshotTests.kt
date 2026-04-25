package com.swahilib.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.swahilib.core.designsystem.component.AppIconToggleButton
import com.swahilib.core.designsystem.icon.AppIcons
import com.swahilib.core.testing.util.captureMultiTheme
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class IconButtonScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun iconButton_multipleThemes() {
        composeTestRule.captureMultiTheme("IconButton") {
            AppIconToggleExample(false)
        }
    }

    @Test
    fun iconButton_unchecked_multipleThemes() {
        composeTestRule.captureMultiTheme("IconButton", "IconButtonUnchecked") {
            Surface {
                AppIconToggleExample(true)
            }
        }
    }

    @Composable
    private fun AppIconToggleExample(checked: Boolean) {
        AppIconToggleButton(
            checked = checked,
            onCheckedChange = { },
            icon = {
                Icon(
                    imageVector = AppIcons.BookmarkBorder,
                    contentDescription = null,
                )
            },
            checkedIcon = {
                Icon(
                    imageVector = AppIcons.Bookmark,
                    contentDescription = null,
                )
            },
        )
    }
}
