package org.mindtec.uikit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.grayCard
import org.mindtec.uikit.ui.theme.neutral600

@Composable
fun CloseButton(
    modifier: Modifier,
    action: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(12.dp)
            .size(32.dp)
            .background(grayCard, CircleShape)
            .clip(CircleShape)
            .clickable { action.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.uikit_ic_clear),
            contentDescription = "Cerrar diálogo",
            tint = neutral600,
            modifier = Modifier.size(20.dp)
        )
    }
}
