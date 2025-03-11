package jp.co.ndk_group.mdk.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
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

private val eyeCloseHold = MdkTarget.EyeCloseHold(MdkSide.Unspecified)
private val eyeCloseRepeat = MdkTarget.EyeCloseRepeat(MdkSide.Unspecified)
private val eyeMovement = MdkTarget.EyeMovement

private val optionsBuilder = MdkOptions.Builder()
    .setEnabledMdkTargets(setOf(eyeCloseHold, eyeCloseRepeat, eyeMovement))
    .setActionParams(
        eyeCloseHold,
        MdkOptions.HorizontalPairedHoldActionParams(
            threshold = 0.7f,
            requiredMillis = { count -> if (count == 1) 1_500 else 2_000 },
        ),
    )
    .setActionParams(
        eyeCloseRepeat,
        MdkOptions.HorizontalPairedRepeatActionParams(
            threshold = 0.7f,
            requiredMillis = 500,
            waitToActionMillis = 1_000,
            tooLongMillis = 1_500,
        )
    )
    .setActionParams(
        eyeMovement,
        MdkOptions.MovementActionParams(
            horizontalSensitivity = 15f,
            verticalSensitivity = 15f,
        )
    )

@Composable
fun MainActivity.App()  {

    val hapticFeedback = LocalHapticFeedback.current

    var holdHistory by remember {
        mutableStateOf(History())
    }

    var repeatHistory by remember {
        mutableStateOf(History())
    }

    var pointerPositionX by remember {
        mutableStateOf(resources.configuration.screenWidthDp.dp / 2)
    }

    var pointerPositionY by remember {
        mutableStateOf(resources.configuration.screenHeightDp.dp / 2)
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
                        pointerPositionX = resources.configuration.screenWidthDp.dp * normalizedMovementState.x
                        pointerPositionY =  resources.configuration.screenHeightDp.dp * normalizedMovementState.y
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