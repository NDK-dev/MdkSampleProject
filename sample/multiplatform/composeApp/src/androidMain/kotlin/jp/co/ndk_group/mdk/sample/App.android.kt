package jp.co.ndk_group.mdk.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
actual fun rememberScreenSize(): DpSize {
    val configuration = LocalConfiguration.current
    val size by remember(configuration) {
        derivedStateOf {
            DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
        }
    }
    return size
}