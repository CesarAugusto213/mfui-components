package org.mindtec.uikit.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.ui.components.items.RadioButtonWithBorder
import org.mindtec.uikit.ui.theme.white

/**
 * Ya era completamente genérico en el original (`<T>`), se migró sin
 * cambios. La única pieza atada a `WarehouseEntity` era un wrapper
 * (`ShowSelectWarehouse`) que no se migró — es trivial de recrear en tu
 * propia app: `FilterDialogMF(options = almacenes, setLabel = { it.nombre }, ...)`.
 */
@Composable
fun <T> FilterDialogMF(
    options: List<T>,
    selectedOption: T,
    setLabel: (T) -> String,
    onClick: (T) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                options.forEach {
                    RadioButtonWithBorder(
                        modifier = Modifier
                            .padding(bottom = 1.dp)
                            .fillMaxWidth(),
                        label = setLabel(it),
                        isSelected = selectedOption == it,
                        onClick = { onClick.invoke(it) }
                    )
                }
            }
        }
    }
}
