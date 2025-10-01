package jp.co.ndk_group.mdk.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jp.co.ndk_group.mdk.MdkOptions
import jp.co.ndk_group.mdk.MdkResult
import jp.co.ndk_group.mdk.MdkTarget
import jp.co.ndk_group.mdk.MdkView
import jp.co.ndk_group.mdk.entity.MdkSide

private val eyeCloseHold = MdkTarget.EyeCloseHold(MdkSide.Unspecified)
private val eyeCloseRepeat = MdkTarget.EyeCloseRepeat(MdkSide.Unspecified)
private val eyeMovement = MdkTarget.EyeMovement
private val fps = MdkTarget.Fps

private val optionsBuilder = MdkOptions.Builder()
    .setEnabledMdkTargets(setOf(
        eyeCloseHold,
        eyeCloseRepeat,
        eyeMovement,
        fps,
    ))
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
fun App()  {

    val hapticFeedback = LocalHapticFeedback.current

    var holdHistory by remember {
        mutableStateOf(History())
    }

    var repeatHistory by remember {
        mutableStateOf(History())
    }

    val configuration = LocalConfiguration.current

    var pointerPositionX by remember {
        mutableStateOf(configuration.screenWidthDp.dp / 2)
    }

    var pointerPositionY by remember {
        mutableStateOf(configuration.screenHeightDp.dp / 2)
    }

    var fpsValue by remember {
        mutableIntStateOf(0)
    }

    Box {
        Column {
            MdkView(
                optionsBuilder
                    .setListener {
                        when (val hold = eyeCloseHold.currentState()) {
                            is MdkResult.ScalarActionState.CountUp -> {
                                holdHistory = holdHistory.copy(
                                    currentCount = hold.count,
                                    lastCount = hold.count
                                )
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            }

                            is MdkResult.ScalarActionState.End -> {
                                if (hold.count > 0) {
                                    holdHistory = holdHistory.copy(
                                        currentCount = null,
                                        lastCount = hold.count,
                                        history = holdHistory.history + hold.count
                                    )
                                }
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
                        pointerPositionX = configuration.screenWidthDp.dp * normalizedMovementState.x
                        pointerPositionY =  configuration.screenHeightDp.dp * normalizedMovementState.y

                        fpsValue = fps.currentState().value.toInt()
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

                HistoryView("hold", holdHistory)
                HistoryView("repeat", repeatHistory)

            }
        }

        Fps(
            fpsValue = { fpsValue },
            modifier = Modifier.align(Alignment.TopStart)
        )

        Pointer(
            pointerPositionX = { pointerPositionX },
            pointerPositionY = { pointerPositionY },
        )

    }
}

@Composable
private fun Fps(
    fpsValue: () -> Int,
    modifier: Modifier = Modifier,
) {
    Text(
        "FPS: ${fpsValue()}",
        style = MaterialTheme.typography.titleLarge.copy(
            shadow = Shadow(
                color = Color.Black,
                blurRadius = 8f,
            )
        ),
        color = Color.White,
        modifier = modifier
            .padding(horizontal = 20.dp)
            .safeDrawingPadding()
    )
}

@Composable
private fun Pointer (
    pointerPositionX: () -> Dp,
    pointerPositionY: () -> Dp,
    modifier: Modifier = Modifier,
) {
    // Pointer
    Box(
        modifier
            .offset(pointerPositionX() - 10.dp, pointerPositionY() - 10.dp)
            .size(20.dp)
            .background(Color.Red, CircleShape)
    )
}