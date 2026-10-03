package org.mindtec.uikit.utils

import org.mindtec.uikit.ui.components.model.ExpandedUiKitProduct
import org.mindtec.uikit.ui.components.model.UiKitProduct
import java.math.BigDecimal

/**
 * Reemplaza a `RestaurantUtils.expandProductsWithComments` del proyecto
 * original. Misma lógica exacta (agrupar por tipo, separar en unidades
 * individuales las que tienen indicaciones y agrupar el resto), pero ya no
 * necesita parsear JSON — `UiKitProduct.commentGroups` llega pre-parseado.
 */
object RestaurantListUtils {

    /**
     * Expande una lista de productos según su cantidad y comentarios.
     * Si un producto tiene cantidad N y comentarios o complementos, lo divide en
     * N items individuales. Los items sin comentarios se agrupan al final. Los
     * productos del mismo tipo (`itemTypeId`) se muestran consecutivamente.
     */
    fun expandProductsWithComments(products: List<UiKitProduct>): List<ExpandedUiKitProduct> {
        val expandedList = mutableListOf<ExpandedUiKitProduct>()

        val productsByType = products.groupBy { it.itemTypeId }

        productsByType.forEach { (_, productsOfSameType) ->
            var totalItemsWithoutComments = 0
            var baseProduct: UiKitProduct? = null

            productsOfSameType.forEach { product ->
                val quantity = product.quantity.toInt()
                val commentGroups = product.commentGroups

                if (baseProduct == null) baseProduct = product

                repeat(quantity) { index ->
                    val itemIndex = index + 1
                    val groupForThisItem = commentGroups.find { it.itemIndex == itemIndex }

                    if (groupForThisItem?.hasSelection == true) {
                        val individualProduct = product.copy(
                            quantity = BigDecimal.ONE,
                            commentGroups = emptyList()
                        )

                        expandedList.add(
                            ExpandedUiKitProduct(
                                product = individualProduct,
                                itemIndex = itemIndex,
                                comments = groupForThisItem.comments,
                                isIndividualItem = true,
                                accessories = groupForThisItem.accessories
                            )
                        )
                    } else {
                        totalItemsWithoutComments++
                    }
                }
            }

            if (totalItemsWithoutComments > 0 && baseProduct != null) {
                val groupedProduct = baseProduct!!.copy(
                    quantity = totalItemsWithoutComments.toBigDecimal(),
                    commentGroups = emptyList()
                )

                expandedList.add(
                    ExpandedUiKitProduct(
                        product = groupedProduct,
                        itemIndex = 1,
                        comments = emptyList(),
                        isIndividualItem = false
                    )
                )
            }
        }

        return expandedList
    }
}
