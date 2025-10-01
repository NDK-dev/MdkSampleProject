package jp.co.ndk_group.mdk.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MdkProject",
    ) {
        App()
    }
}