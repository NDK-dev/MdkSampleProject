package jp.co.ndk_group.mdk.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import jp.co.ndk_group.mdk.entity.MdkSide
import org.jetbrains.compose.ui.tooling.preview.Preview


val eyeCloseHold = MdkTarget.EyeCloseHold(MdkSide.Unspecified)
val eyeCloseRepeat = MdkTarget.EyeCloseRepeat(MdkSide.Unspecified)
val eyeMovement = MdkTarget.EyeMovement

val optionsBuilder = MdkOptions.Builder()
    .setEnabledMdkTargets(
        setOf(
            eyeCloseHold,
            eyeCloseRepeat,
            eyeMovement,
        )
    )
    .setActionParams(
        eyeCloseHold,
        MdkOptions.HorizontalPairedHoldActionParams(
            thresholdRatio = { side ->
                when (side) {
                    MdkSide.Left -> 0.6f
                    MdkSide.Right -> 0.6f
                }
            },
            requiredMillis = { count -> if (count == 1) 500 else 1_000 },
        ),
    )
    .setActionParams(
        eyeCloseRepeat,
        MdkOptions.HorizontalPairedRepeatActionParams(
            thresholdRatio = { side ->
                when (side) {
                    MdkSide.Left -> 0.6f
                    MdkSide.Right -> 0.6f
                }
            },
            requiredMillis = 50,
            waitToActionMillis = 500,
            tooLongMillis = 500,
        )
    )
    .setActionParams(
        eyeMovement,
        MdkOptions.MovementActionParams(
            blinkThresholdRatio = { side ->
                when (side) {
                    MdkSide.Left -> 0.6f
                    MdkSide.Right -> 0.6f
                }
            },
            sensitivityFactor = {
                when (it) {
                    MdkSide.Axis.Horizontal -> 1f
                    MdkSide.Axis.Vertical -> 1f
                }
            },
        )
    )

@Composable
@Preview
fun App() {
    MaterialTheme {

        val hapticFeedback = LocalHapticFeedback.current

        var holdHistory by remember {
            mutableStateOf(History<Int>())
        }

        var repeatHistory by remember {
            mutableStateOf(History<Int>())
        }

        val size = rememberScreenSize()

        var pointerPositionX by remember {
            mutableStateOf(size.width / 2)
        }

        var pointerPositionY by remember {
            mutableStateOf(size.height / 2)
        }

        Box {
            Column {
                MdkView(
                    optionsBuilder
                        .setListener {
                            when (val hold = eyeCloseHold.currentState()) {
                                is MdkResult.ScalarActionState.CountUp -> {
                                    holdHistory = holdHistory.copy(
                                        currentValue = hold.count,
                                        lastValue = hold.count
                                    )
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                }

                                is MdkResult.ScalarActionState.End -> {
                                    holdHistory = holdHistory.copy(
                                        currentValue = null,
                                        lastValue = hold.count,
                                        history = holdHistory.history + hold.count
                                    )
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                }

                                is MdkResult.ScalarActionState.None -> {
                                    holdHistory = holdHistory.copy(
                                        currentValue = null,
                                    )
                                }

                                else -> {
                                }
                            }
                            when (val repeat = getState(eyeCloseRepeat)) {
                                is MdkResult.ScalarActionState.CountUp -> {
                                    repeatHistory = repeatHistory.copy(
                                        currentValue = repeat.count,
                                        lastValue = repeat.count
                                    )
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                }

                                is MdkResult.ScalarActionState.End -> {
                                    repeatHistory = repeatHistory.copy(
                                        currentValue = null,
                                        lastValue = repeat.count,
                                        history = repeatHistory.history + repeat.count
                                    )
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                }

                                is MdkResult.ScalarActionState.None -> {
                                    repeatHistory = repeatHistory.copy(
                                        currentValue = null,
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
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp)
                        .weight(1f)
                ) {

                    HistoryView("hold", holdHistory, Modifier)
                    HistoryView("repeat", repeatHistory, Modifier)
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

data class History<T: Any>(
    val currentValue: T? = null,
    val lastValue: T? = null,
    val history: List<T> = emptyList(),
)

@Composable
fun <T: Any> HistoryView(
    name: String,
    history: History<T>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        Text(name, style = MaterialTheme.typography.h6)

        Row {
            Text("current: ${history.currentValue},")
            Text("last: ${history.lastValue}")
        }

        Text(
            "history: ${history.history.take(10).joinToString(",")}",
        )

    }
}
