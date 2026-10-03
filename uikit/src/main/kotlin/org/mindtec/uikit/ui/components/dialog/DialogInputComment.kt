package org.mindtec.uikit.ui.components.dialog

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.mindtec.uikit.R
import org.mindtec.uikit.ui.components.button.SmallOutlinedButtonMF
import org.mindtec.uikit.ui.components.model.UiKitAccessory
import org.mindtec.uikit.ui.components.model.UiKitComment
import org.mindtec.uikit.ui.components.model.UiKitCommentGroup
import org.mindtec.uikit.ui.components.model.UiKitProduct
import org.mindtec.uikit.ui.theme.*
import org.mindtec.uikit.utils.Fonts
import org.mindtec.uikit.utils.restrictDigits
import java.math.BigDecimal

/**
 * Selector de indicaciones de cocina para un producto (ej. "sin cebolla"),
 * con tabs por unidad cuando la cantidad es mayor a 1.
 *
 * Todo el estado que depende de red (listar comentarios por categoría,
 * guardar uno nuevo) se recibe por parámetro y sale por callbacks —
 * responsabilidad de la app que usa el componente. El estado de
 * selección/tabs/modo-de-escritura es interno, por ser pura interacción
 * de UI.
 *
 * @param product Producto para el que se están eligiendo indicaciones.
 * @param quantity Cantidad de unidades del producto (define cuántos tabs
 * se muestran).
 * @param category Categoría de indicaciones a mostrar/guardar en.
 * @param availableComments Indicaciones disponibles para elegir en esta
 * categoría (la app las carga antes de mostrar el diálogo).
 * @param isLoading `true` mientras se guarda una indicación nueva.
 * @param saveErrorMessage Mensaje a mostrar si falló el guardado de una
 * indicación nueva.
 * @param lastSavedComment Indicación recién creada por [onRequestNewComment]
 * — cuando cambia, se agrega y selecciona automáticamente en el tab activo
 * (igual que hacía el original al completar el guardado).
 * @param onRequestNewComment La app crea la indicación (llamada de red) y
 * la refleja en [availableComments] / [lastSavedComment].
 * @param availableAccessories Complementos con precio que se pueden sumar
 * por unidad. Si viene vacío, el diálogo solo muestra notas (igual que antes).
 * @param currencySymbol Símbolo para los precios de complementos y el resumen.
 * @param onConfirm Se llama al aceptar, con el producto y lo elegido por
 * unidad (notas y complementos en cada [UiKitCommentGroup]).
 *
 * Con más de una unidad aparece "Aplicar a todas": copia lo elegido en la
 * unidad actual (de la sección visible) al resto de unidades.
 */
@SuppressLint("MutableCollectionMutableState")
@Composable
fun DialogInputComment(
    product: UiKitProduct,
    quantity: Int,
    category: String,
    title: String = stringResource(R.string.uikit_kitchen_indications),
    availableComments: List<UiKitComment>,
    isLoading: Boolean = false,
    saveErrorMessage: String? = null,
    lastSavedComment: UiKitComment? = null,
    availableAccessories: List<UiKitAccessory> = emptyList(),
    currencySymbol: String = "S/",
    onDismissRequest: () -> Unit,
    onRequestNewComment: (category: String, text: String) -> Unit,
    onConfirm: (product: UiKitProduct, commentGroups: List<UiKitCommentGroup>) -> Unit
) {
    var commentGroups by remember { mutableStateOf(listOf<UiKitCommentGroup>()) }
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    var showInputMode by remember { mutableStateOf(false) }
    var showAccessories by remember { mutableStateOf(false) }
    val hasAccessories = availableAccessories.isNotEmpty()
    var newComment by remember { mutableStateOf("") }

    val annotatedText = buildAnnotatedString {
        append(stringResource(R.string.uikit_comment_hint))

        pushStringAnnotation(tag = "NEW", annotation = "NEW")
        withStyle(
            style = SpanStyle(
                color = red200,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append(stringResource(R.string.uikit_comment_new_label))
        }
        pop()
    }

    LaunchedEffect(product, quantity) {
        val existingGroups = product.commentGroups

        commentGroups = (1..quantity).map { index ->
            existingGroups.find { it.itemIndex == index } ?: UiKitCommentGroup(itemIndex = index)
        }
        selectedItemIndex = 0
    }

    LaunchedEffect(lastSavedComment) {
        lastSavedComment?.let { comment ->
            commentGroups = commentGroups.mapIndexed { index, group ->
                if (index == selectedItemIndex) {
                    group.copy(comments = group.comments + comment.copy(isSelected = true))
                } else {
                    group
                }
            }
            showInputMode = false
        }
    }

    fun saveComment() {
        if (newComment.trim().isNotEmpty()) {
            onRequestNewComment.invoke(category, newComment.trim())
            newComment = ""
        }
    }

    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white)
                    .padding(20.dp),
                verticalArrangement = if (showInputMode) Arrangement.Center else Arrangement.spacedBy(
                    16.dp
                ),
                horizontalAlignment = if (showInputMode) Alignment.CenterHorizontally else Alignment.Start
            ) {
                Text(
                    text = title,
                    style = Fonts.textRegular(14.sp)
                )

                when {
                    showInputMode -> CommentInputSection(
                        newComment = newComment,
                        saveMessage = saveErrorMessage,
                        onCancel = {
                            showInputMode = false
                            newComment = ""
                        },
                        onSave = { saveComment() },
                        onTextChange = { newComment = it }
                    )

                    isLoading -> LoaderCustom(true)

                    // Sin complementos y sin notas: mismo estado vacío de siempre.
                    !hasAccessories && availableComments.isEmpty() -> CommentEmptyState(onNewComment = {
                        showInputMode = true
                    })

                    else -> {
                        Text(
                            text = stringResource(R.string.uikit_quantity_label, quantity),
                            style = Fonts.textRegular(14.sp)
                        )

                        if (hasAccessories) {
                            SectionSwitch(
                                showAccessories = showAccessories,
                                notesCount = commentGroups.sumOf { it.comments.size },
                                accessoriesCount = commentGroups.sumOf { it.accessories.size },
                                onSelect = { showAccessories = it }
                            )
                        }

                        TabsSection(
                            quantity = quantity,
                            selectedIndex = selectedItemIndex,
                            commentGroups = commentGroups,
                            onTabSelected = { selectedItemIndex = it }
                        )

                        val currentGroup = commentGroups.getOrNull(selectedItemIndex)
                        val currentHasSelection = if (showAccessories) {
                            currentGroup?.accessories?.isNotEmpty() == true
                        } else {
                            currentGroup?.comments?.isNotEmpty() == true
                        }
                        if (quantity > 1 && currentHasSelection) {
                            SmallOutlinedButtonMF(
                                modifier = Modifier.align(Alignment.End),
                                text = stringResource(R.string.uikit_apply_to_all),
                                textColor = UiKitTheme.colors.primary,
                                border = UiKitTheme.colors.primary,
                                onClick = {
                                    val source = currentGroup ?: return@SmallOutlinedButtonMF
                                    commentGroups = commentGroups.map { group ->
                                        if (showAccessories) group.copy(accessories = source.accessories)
                                        else group.copy(comments = source.comments)
                                    }
                                }
                            )
                        }

                        when {
                            showAccessories -> AccessoryListSection(
                                availableAccessories = availableAccessories,
                                selectedAccessories = currentGroup?.accessories.orEmpty(),
                                currencySymbol = currencySymbol,
                                onToggleSelection = { accessory, isChecked ->
                                    commentGroups = commentGroups.mapIndexed { index, group ->
                                        if (index != selectedItemIndex) group
                                        else if (isChecked) group.copy(accessories = group.accessories + accessory)
                                        else group.copy(accessories = group.accessories.filter { it.id != accessory.id })
                                    }
                                }
                            )

                            availableComments.isEmpty() -> CommentEmptyState(onNewComment = {
                                showInputMode = true
                            })

                            else -> CommentListSection(
                                availableComments = availableComments,
                                selectedComments = currentGroup?.comments ?: emptyList(),
                                annotatedText = annotatedText,
                                onNewComment = { showInputMode = true },
                                onToggleSelection = { comment, isChecked ->
                                    val updatedComments = if (isChecked) {
                                        currentGroup?.comments.orEmpty() + comment.copy(isSelected = true)
                                    } else {
                                        currentGroup?.comments.orEmpty().filter { it.id != comment.id }
                                    }

                                    commentGroups = commentGroups.mapIndexed { index, group ->
                                        if (index == selectedItemIndex) {
                                            group.copy(comments = updatedComments)
                                        } else {
                                            group
                                        }
                                    }
                                }
                            )
                        }

                        if (hasAccessories) {
                            PriceSummary(
                                unitPrice = product.unitPrice,
                                quantity = quantity,
                                accessoriesTotal = commentGroups.sumOf { group ->
                                    group.accessories.fold(BigDecimal.ZERO) { acc, item -> acc + item.price }
                                },
                                currencySymbol = currencySymbol
                            )
                        }
                    }
                }

                if (!showInputMode && !isLoading) {
                    SmallOutlinedButtonMF(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        text = stringResource(R.string.uikit_accept),
                        textColor = white,
                        textSize = 16.sp,
                        border = darkButton,
                        background = darkButton,
                        onClick = {
                            onConfirm(product, commentGroups)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentInputSection(
    newComment: String,
    saveMessage: String?,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onTextChange: (String) -> Unit
) {
    Spacer(modifier = Modifier.size(12.dp))
    Text(
        text = stringResource(R.string.uikit_comment_input_hint),
        style = Fonts.textLight(12.sp, grayIndicator)
    )
    Spacer(modifier = Modifier.size(8.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = newComment,
        onValueChange = onTextChange,
        minLines = 3,
        maxLines = 5,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onSave() })
    )

    saveMessage?.let {
        Text(
            text = it,
            style = Fonts.textRegular(12.sp, red200),
            modifier = Modifier.padding(top = 8.dp)
        )
    }

    Spacer(modifier = Modifier.size(20.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SmallOutlinedButtonMF(
            modifier = Modifier
                .height(50.dp)
                .weight(1f),
            text = stringResource(R.string.uikit_cancel),
            textSize = 16.sp,
            border = darkButton,
            textColor = darkButton,
            onClick = onCancel
        )
        SmallOutlinedButtonMF(
            modifier = Modifier
                .height(50.dp)
                .weight(1f),
            text = stringResource(R.string.uikit_save),
            textColor = white,
            textSize = 16.sp,
            border = darkButton,
            background = darkButton,
            onClick = onSave
        )
    }
}

@Composable
private fun CommentEmptyState(onNewComment: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("!", style = Fonts.textSemiBold(32.sp, grayIndicator))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.uikit_comment_empty_elements),
            style = Fonts.textRegular(14.sp, grayIndicator)
        )
        Row {
            Text(
                text = stringResource(R.string.uikit_comment_add_note),
                style = Fonts.textRegular(14.sp, grayIndicator)
            )
            Text(
                modifier = Modifier.clickable { onNewComment() },
                text = stringResource(R.string.uikit_comment_new_label),
                style = Fonts.textSemiBold(14.sp, red200)
            )
        }
    }
}

@Composable
private fun LoaderCustom(isLoading: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .rotate(rotation),
                painter = painterResource(R.drawable.uikit_ic_loading),
                contentDescription = null,
                colorFilter = ColorFilter.tint(UiKitTheme.colors.primary)
            )
        }
    }
}

@Composable
private fun TabsSection(
    quantity: Int,
    selectedIndex: Int,
    commentGroups: List<UiKitCommentGroup>,
    onTabSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(quantity) { index ->
            val hasComments = commentGroups.getOrNull(index)?.hasSelection == true

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when {
                            index == selectedIndex -> UiKitTheme.colors.primary
                            hasComments -> UiKitTheme.colors.primaryContainer
                            else -> lightGrayPlaceholder
                        },
                        shape = CircleShape
                    )
                    .border(
                        width = if (index == selectedIndex) 2.dp else 1.dp,
                        color = when {
                            index == selectedIndex -> UiKitTheme.colors.primary
                            hasComments -> UiKitTheme.colors.primaryContainerAlt
                            else -> neutral300
                        },
                        shape = CircleShape
                    )
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    style = Fonts.textSemiBold(
                        14.sp,
                        when {
                            index == selectedIndex -> white
                            hasComments -> UiKitTheme.colors.primaryPressed
                            else -> neutral700
                        }
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun CommentListSection(
    availableComments: List<UiKitComment>,
    selectedComments: List<UiKitComment>,
    annotatedText: AnnotatedString,
    onNewComment: () -> Unit,
    onToggleSelection: (UiKitComment, Boolean) -> Unit
) {
    val scrollState = rememberScrollState()

    Text(
        modifier = Modifier.padding(vertical = 8.dp),
        text = stringResource(R.string.uikit_comment_select_option),
        style = Fonts.textRegular(14.sp)
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
    ) {
        val containerHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val thumbHeightPx = containerHeightPx * 0.15f
        val maxScroll = scrollState.maxValue.toFloat().coerceAtLeast(1f)
        val scrollProgress = scrollState.value / maxScroll
        val thumbOffsetPx = (containerHeightPx - thumbHeightPx) * scrollProgress
        val isAllContentDisplay = scrollState.maxValue == 0

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(end = if (!isAllContentDisplay) 6.dp else 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableComments.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { item ->
                        val isSelected = selectedComments.any { it.id == item.id }
                        Box(modifier = Modifier.weight(1f)) {
                            CommentItem(
                                comment = item,
                                isSelected = isSelected,
                                onCheckedChange = onToggleSelection
                            )
                        }
                    }

                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        if (!isAllContentDisplay) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset { IntOffset(0, thumbOffsetPx.toInt()) }
                    .width(4.dp)
                    .height(with(LocalDensity.current) { thumbHeightPx.toDp() })
                    .background(UiKitTheme.colors.primary, RoundedCornerShape(2.dp))
            )
        }
    }

    ClickableText(
        text = annotatedText,
        style = Fonts.textRegular(14.sp, grayIndicator),
        modifier = Modifier.padding(top = 8.dp),
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "NEW", start = offset, end = offset)
                .firstOrNull()?.let {
                    onNewComment()
                }
        }
    )
}

@Composable
private fun CommentItem(
    comment: UiKitComment,
    isSelected: Boolean,
    onCheckedChange: (UiKitComment, Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = Modifier.size(20.dp),
            checked = isSelected,
            onCheckedChange = { onCheckedChange(comment, it) },
            colors = CheckboxDefaults.colors(
                checkedColor = blue300,
                uncheckedColor = grayIndicator,
                checkmarkColor = white
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = comment.description,
            overflow = TextOverflow.Ellipsis,
            style = Fonts.textRegular(12.sp)
        )
    }
}

@Composable
private fun SectionSwitch(
    showAccessories: Boolean,
    notesCount: Int,
    accessoriesCount: Int,
    onSelect: (showAccessories: Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            false to stringResource(R.string.uikit_tab_with_count, stringResource(R.string.uikit_notes_tab), notesCount),
            true to stringResource(R.string.uikit_tab_with_count, stringResource(R.string.uikit_accessories_tab), accessoriesCount)
        ).forEach { (isAccessories, label) ->
            val selected = showAccessories == isAccessories
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(if (selected) UiKitTheme.colors.primary else white, RoundedCornerShape(8.dp))
                    .border(1.dp, if (selected) UiKitTheme.colors.primary else neutral300, RoundedCornerShape(8.dp))
                    .clickable { onSelect(isAccessories) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Fonts.textSemiBold(13.sp, if (selected) white else neutral700)
                )
            }
        }
    }
}

@Composable
private fun AccessoryListSection(
    availableAccessories: List<UiKitAccessory>,
    selectedAccessories: List<UiKitAccessory>,
    currencySymbol: String,
    onToggleSelection: (UiKitAccessory, Boolean) -> Unit
) {
    Text(
        modifier = Modifier.padding(vertical = 8.dp),
        text = stringResource(R.string.uikit_accessories_select_option),
        style = Fonts.textRegular(14.sp)
    )
    if (availableAccessories.isEmpty()) {
        Text(
            text = stringResource(R.string.uikit_accessories_empty),
            style = Fonts.textRegular(14.sp, grayIndicator)
        )
        return
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        availableAccessories.forEach { accessory ->
            val isSelected = selectedAccessories.any { it.id == accessory.id }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleSelection(accessory, !isSelected) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = isSelected,
                    onCheckedChange = { onToggleSelection(accessory, it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = blue300,
                        uncheckedColor = grayIndicator,
                        checkmarkColor = white
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = accessory.name,
                    overflow = TextOverflow.Ellipsis,
                    style = Fonts.textRegular(12.sp)
                )
                Text(
                    text = stringResource(R.string.uikit_amount_added, currencySymbol, accessory.price.restrictDigits().toPlainString()),
                    style = Fonts.textSemiBold(12.sp, neutral700)
                )
            }
        }
    }
}

@Composable
private fun PriceSummary(
    unitPrice: BigDecimal,
    quantity: Int,
    accessoriesTotal: BigDecimal,
    currencySymbol: String
) {
    val productTotal = unitPrice.multiply(quantity.toBigDecimal())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(lightGrayPlaceholder, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SummaryRow(
            label = stringResource(R.string.uikit_summary_product, quantity),
            value = stringResource(R.string.uikit_amount, currencySymbol, productTotal.restrictDigits().toPlainString())
        )
        if (accessoriesTotal > BigDecimal.ZERO) {
            SummaryRow(
                label = stringResource(R.string.uikit_summary_accessories),
                value = stringResource(R.string.uikit_amount_added, currencySymbol, accessoriesTotal.restrictDigits().toPlainString())
            )
        }
        SummaryRow(
            label = stringResource(R.string.uikit_summary_total),
            value = stringResource(R.string.uikit_amount, currencySymbol, productTotal.add(accessoriesTotal).restrictDigits().toPlainString()),
            bold = true
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String, bold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = if (bold) Fonts.textSemiBold(13.sp, neutral900) else Fonts.textRegular(13.sp, neutral700)
        )
        Text(
            text = value,
            style = if (bold) Fonts.textSemiBold(13.sp, neutral900) else Fonts.textRegular(13.sp, neutral700)
        )
    }
}
