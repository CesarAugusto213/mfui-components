package org.mindtec.uikit.ui.components.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.theme.blue300

@Composable
fun OutlineIconButtonMF(
    modifier: Modifier = Modifier,
    icon: Painter,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .border(width = 1.dp, color = blue300, shape = RoundedCornerShape(8.dp))
            .size(35.dp)
            .padding(
                horizontal = 8.dp,
                vertical = 0.dp
            )
    ) {
        IconButton(onClick = { onClick.invoke() }) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(35.dp),
                tint = blue300
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OutlineIconButtonMFPreview() {
    OutlineIconButtonMF(
        icon = painterResource(R.drawable.uikit_ic_edit),
        modifier = Modifier.padding(16.dp)
    ) {}
}
