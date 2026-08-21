package org.mindtec.uikit.ui.components.model

import java.math.BigDecimal

/**
 * Modelo genérico de producto que consumen los componentes de listas de
 * productos/restaurante del kit (`TableProductList`, `ItemProductRestaurantList`,
 * `TableRestaurantProductList`, `TableDialog`, `TableRestaurantDialog`).
 *
 * Reemplaza al `ProductEntity` (Room) del proyecto original. Cada app mapea
 * su propia entidad a esto antes de pasarlo a los componentes — el mapeo es
 * responsabilidad de la app porque ahí es donde vive la lógica específica
 * (ej. cómo se arma el texto de presentación, qué campo usar como id cuando
 * el producto todavía no tiene id de servidor, etc.).
 *
 * @param id Identificador único a usar para borrar/ubicar el item en la
 * lista (en el original era `if (id == 0) internalId else id.toString()` —
 * esa resolución la hace la app antes de construir este modelo).
 * @param itemTypeId Clave para agrupar unidades del mismo producto (en el
 * original era `itemId`). Se usa solo para la expansión por comentarios.
 * @param description Texto ya resuelto para mostrar (incluye cualquier
 * sufijo de presentación que la app quiera agregar — el kit ya no parsea
 * JSON de presentación).
 * @param quantity Cantidad de este producto.
 * @param unitPrice Precio unitario de venta.
 * @param imageUrl URL de la imagen del producto (opcional).
 * @param commentGroups Indicaciones de cocina ya agrupadas por unidad
 * (en el original venía serializado como JSON en `comment: String?`; acá
 * se recibe ya parseado — la app se encarga de su propia serialización si
 * necesita persistirlo).
 */
data class UiKitProduct(
    val id: String,
    val itemTypeId: String,
    val description: String,
    val quantity: BigDecimal,
    val unitPrice: BigDecimal,
    val imageUrl: String? = null,
    val commentGroups: List<UiKitCommentGroup> = emptyList()
)
