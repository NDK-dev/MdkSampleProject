package jp.co.ndk_group.mdk.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.toSize

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun rememberScreenSize(): DpSize {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val size by remember(density, windowInfo) {
        derivedStateOf {
            with(density) {
                windowInfo.containerSize.toSize().toDpSize()
            }
        }
    }
    return size
}