package org.mindtec.uikit.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mindtec.uikit.ui.components.model.UiKitComment
import org.mindtec.uikit.ui.components.model.UiKitCommentGroup
import org.mindtec.uikit.ui.components.model.UiKitProduct
import java.math.BigDecimal

class RestaurantListUtilsTest {

    private fun comment(id: Int, description: String) =
        UiKitComment(id = id, description = description, categoryId = 1)

    @Test
    fun `product without comments stays grouped as one line`() {
        val product = UiKitProduct(
            id = "1",
            itemTypeId = "10",
            description = "GASEOSA",
            quantity = BigDecimal(3),
            unitPrice = BigDecimal("5.00")
        )

        val result = RestaurantListUtils.expandProductsWithComments(listOf(product))

        assertEquals(1, result.size)
        assertEquals(BigDecimal(3), result.first().product.quantity)
        assertTrue(!result.first().isIndividualItem)
        assertEquals(0, result.first().comments.size)
    }

    @Test
    fun `units with comments are split into individual items`() {
        val product = UiKitProduct(
            id = "1",
            itemTypeId = "10",
            description = "HAMBURGUESA",
            quantity = BigDecimal(2),
            unitPrice = BigDecimal("18.50"),
            commentGroups = listOf(
                UiKitCommentGroup(itemIndex = 1, comments = listOf(comment(1, "Sin cebolla")))
            )
        )

        val result = RestaurantListUtils.expandProductsWithComments(listOf(product))

        assertEquals(2, result.size)

        val individual = result.first { it.isIndividualItem }
        assertEquals(BigDecimal.ONE, individual.product.quantity)
        assertEquals(1, individual.comments.size)
        assertEquals("Sin cebolla", individual.comments.first().description)

        val grouped = result.first { !it.isIndividualItem }
        assertEquals(BigDecimal.ONE, grouped.product.quantity)
        assertTrue(grouped.comments.isEmpty())
    }

    @Test
    fun `products of the same type but different rows are merged`() {
        val base = UiKitProduct(
            id = "1",
            itemTypeId = "10",
            description = "GASEOSA",
            quantity = BigDecimal.ONE,
            unitPrice = BigDecimal("5.00")
        )

        val result = RestaurantListUtils.expandProductsWithComments(listOf(base, base.copy(id = "2")))

        assertEquals(1, result.size)
        assertEquals(BigDecimal(2), result.first().product.quantity)
    }

    @Test
    fun `empty list returns empty result`() {
        assertTrue(RestaurantListUtils.expandProductsWithComments(emptyList()).isEmpty())
    }
}
