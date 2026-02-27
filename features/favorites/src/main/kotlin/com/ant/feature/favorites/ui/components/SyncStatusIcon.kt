package com.ant.feature.favorites.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ant.resources.R as R2

@Composable
fun SyncStatusIcon(
    isSynced: Boolean,
    isSyncing: Boolean,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        isSyncing -> {
            CircularProgressIndicator(
                modifier = modifier
                    .size(18.dp)
                    .padding(start = 4.dp),
                strokeWidth = 2.dp,
            )
        }
        isSynced -> {
            Icon(
                imageVector = Icons.Outlined.CloudDone,
                contentDescription = stringResource(R2.string.sync_to_cloud),
                modifier = modifier
                    .size(18.dp)
                    .padding(start = 4.dp),
                tint = Color(0xFF4CAF50),
            )
        }
        else -> {
            Icon(
                imageVector = Icons.Outlined.CloudOff,
                contentDescription = stringResource(R2.string.sync_local_only),
                modifier = modifier
                    .size(18.dp)
                    .padding(start = 4.dp)
                    .clickable(onClick = onSyncClick),
                tint = Color(0xFFFF9800),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SyncStatusIconSyncedPreview() {
    MaterialTheme {
        SyncStatusIcon(isSynced = true, isSyncing = false, onSyncClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun SyncStatusIconLocalPreview() {
    MaterialTheme {
        SyncStatusIcon(isSynced = false, isSyncing = false, onSyncClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun SyncStatusIconSyncingPreview() {
    MaterialTheme {
        SyncStatusIcon(isSynced = false, isSyncing = true, onSyncClick = {})
    }
}
