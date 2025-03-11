package jp.co.ndk_group.mdk.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
    Column(modifier) {

        Text(name, Modifier.weight(1f))

        Row(Modifier.weight(1f)) {
            Text("current: ${history.currentCount},")
            Text("last: ${history.lastCount}")
        }

        Text(
            "history: ${history.history.joinToString(",")}",
            Modifier.weight(1f),
        )

    }
}