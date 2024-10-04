package jp.co.ndk_group.mdk.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import jp.co.ndk_group.mdk.MdkOptions
import jp.co.ndk_group.mdk.MdkResult
import jp.co.ndk_group.mdk.MdkTarget
import jp.co.ndk_group.mdk.MdkView
import jp.co.ndk_group.mdk.Side
import org.jetbrains.compose.ui.tooling.preview.Preview


val eyeCloseHold = MdkTarget.EyeCloseHold(Side.Unspecified)
val eyeCloseRepeat = MdkTarget.EyeCloseRepeat(Side.Unspecified)
val eyeMovement = MdkTarget.EyeMovement

val optionsBuilder = MdkOptions.Builder()
    .setEnabledMdkTargets(
        setOf(
            eyeCloseHold,
            eyeCloseRepeat,
//            eyeMovement, // TODO: 要内部実装。
        )
    )
    .setActionParams(
        eyeCloseHold,
        MdkOptions.HorizontalPairedHoldActionParams(
            threshold = 0.5f,
            requiredMillis = { count -> if (count == 1) 1_000 else 1_500 },
        ),
    )
    .setActionParams(
        eyeCloseRepeat,
        MdkOptions.HorizontalPairedRepeatActionParams(
            threshold = 0.5f,
            requiredMillis = 500,
            waitToActionMillis = 1_000,
            tooLongMillis = 1_000,
        )
    )

@Composable
@Preview
fun App() {
    MaterialTheme {

        val hapticFeedback = LocalHapticFeedback.current

        var holdHistory by remember {
            mutableStateOf(History())
        }

        var repeatHistory by remember {
            mutableStateOf(History())
        }

        val size = rememberScreenSize()

        var pointerPositionX by remember {
            mutableStateOf(size.width / 2)
        }

        var pointerPositionY by remember {
            mutableStateOf(size.height / 2)
        }

        val density = LocalDensity.current
        Box {
            Column {
                MdkView(
                    optionsBuilder
                        .setActionParams(
                            eyeMovement,
                            MdkOptions.MovementActionParams(
                                horizontalSensitivity = size.width.value,
                                verticalSensitiviy = size.height.value,
                                areaWidth = with(density) { size.width.toPx().toInt() },
                                areaHeight = with(density) { size.height.toPx().toInt() },
                            )
                        )
                        .setListener {
                            when (val hold = eyeCloseHold.currentState()) {
                                is MdkResult.ScalarActionState.CountUp -> {
                                    holdHistory = holdHistory.copy(
                                        currentCount = hold.count,
                                        lastCount = hold.count
                                    )
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                }

                                is MdkResult.ScalarActionState.End -> {
                                    holdHistory = holdHistory.copy(
                                        currentCount = null,
                                        lastCount = hold.count,
                                        history = holdHistory.history + hold.count
                                    )
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                }

                                is MdkResult.ScalarActionState.None -> {
                                    holdHistory = holdHistory.copy(
                                        currentCount = null,
                                    )
                                }

                                else -> {
                                }
                            }
                            when (val repeat = getState(eyeCloseRepeat)) {
                                is MdkResult.ScalarActionState.CountUp -> {
                                    repeatHistory = repeatHistory.copy(
                                        currentCount = repeat.count,
                                        lastCount = repeat.count
                                    )
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                }

                                is MdkResult.ScalarActionState.End -> {
                                    repeatHistory = repeatHistory.copy(
                                        currentCount = null,
                                        lastCount = repeat.count,
                                        history = repeatHistory.history + repeat.count
                                    )
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                }

                                is MdkResult.ScalarActionState.None -> {
                                    repeatHistory = repeatHistory.copy(
                                        currentCount = null,
                                    )
                                }

                                else -> {
                                }
                            }

                            val normalizedMovementState = eyeMovement.currentState()
                            pointerPositionX = (size.width * normalizedMovementState.x)
                            pointerPositionY = (size.height * normalizedMovementState.y)
                        }
                        .build(),
                    Modifier.weight(1f),
                )

                Column(
                    Modifier.weight(1f)
                ) {

                    HistoryView("hold", holdHistory, Modifier.weight(1f))
                    HistoryView("repeat", repeatHistory, Modifier.weight(1f))

                }

            }


            Box(
                Modifier
                    .offset(pointerPositionX - 10.dp, pointerPositionY - 10.dp)
                    .size(20.dp)
                    .background(Color.Red, CircleShape)

            )

        }
    }
}

@Composable
expect fun rememberScreenSize(): DpSize

data class History(
    val currentCount: Int? = null,
    val lastCount: Int? = null,
    val history: List<Int> = emptyList(),
)

@Composable
fun HistoryView(
    name: String,
    history: History,
    modifier: Modifier = Modifier,
) {
    Column(modifier.onGloballyPositioned {
    }) {

        Text(name, Modifier.weight(1f))

        Row(Modifier.weight(1f)) {
            Text("current: ${history.currentCount},")
            Text("last: ${history.lastCount}")
        }

        Text(
            "histry: ${history.history.joinToString(",")}",
            Modifier.weight(1f),
        )

    }
}