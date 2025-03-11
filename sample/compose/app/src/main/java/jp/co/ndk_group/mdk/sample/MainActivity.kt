package jp.co.ndk_group.mdk.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import jp.co.ndk_group.mdk.MdkOptions
import jp.co.ndk_group.mdk.MdkResult
import jp.co.ndk_group.mdk.MdkTarget
import jp.co.ndk_group.mdk.MdkView
import jp.co.ndk_group.mdk.entity.MdkSide
import jp.co.ndk_group.mdk.sample.ui.theme.MdkSampleProjectTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MdkSampleProjectTheme {
                App()
            }
        }
    }
}

