package lab2

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ProductSelectionTest {

    private fun product(
        id: Long,
        name: String,
        available: Boolean = true
    ) = Product(id = id, name = name, available = available)

    @Test
    fun `selects available products in recommendation order`() {
        val products = listOf(
            product(1, "Keyboard"),
            product(2, "Mouse", available = false),
            product(3, "Monitor"),
            product(4, "Webcam")
        )
        val recommendedIds = listOf(3L, 2L, 3L, 99L, 1L, 4L)
        val excludedIds = listOf(1L)

        val expected = listOf(
            product(3, "Monitor"),
            product(4, "Webcam")
        )
        assertEquals(expected, selectProducts(products, recommendedIds, excludedIds))
        assertEquals(expected, selectProducts(products.reversed(), recommendedIds, excludedIds))
    }

    @Test
    fun `duplicate recommendations and exclusions are handled`() {
        val products = listOf(
            product(1, "A"),
            product(2, "B"),
            product(3, "C")
        )
        val recommendedIds = listOf(1L, 1L, 2L, 1L, 3L, 2L)
        val excludedIds = listOf(2L, 2L, 100L)

        val result = selectProducts(products, recommendedIds, excludedIds)

        assertEquals(
            listOf(product(1, "A"), product(3, "C")),
            result
        )
    }

    @Test
    fun `unavailable excluded and unknown products are skipped`() {
        val products = listOf(
            product(1, "A"),
            product(2, "B", available = false),
            product(3, "C")
        )
        val recommendedIds = listOf(99L, 2L, 1L, 42L, 3L)
        val excludedIds = listOf(1L)

        // 99 и 42 — нет в каталоге, 2 — недоступен, 1 — исключён
        assertEquals(
            listOf(product(3, "C")),
            selectProducts(products, recommendedIds, excludedIds)
        )
    }

    @Test
    fun `empty catalog recommendations or exclusions produce empty result`() {
        assertTrue(selectProducts(emptyList(), listOf(1L), emptyList()).isEmpty())
        assertTrue(selectProducts(listOf(product(1, "A")), emptyList(), emptyList()).isEmpty())
        assertTrue(selectProducts(emptyList(), emptyList(), emptyList()).isEmpty())
        assertTrue(selectProducts(emptyList(), emptyList(), listOf(1L)).isEmpty())
    }

    @Test
    fun `returns empty when no product fits`() {
        val products = listOf(
            product(1, "A", available = false),
            product(2, "B", available = false)
        )

        assertTrue(
            selectProducts(
                products,
                recommendedIds = listOf(1L, 2L),
                excludedIds = emptyList()
            ).isEmpty()
        )

        assertTrue(
            selectProducts(
                listOf(product(1, "A")),
                recommendedIds = listOf(1L),
                excludedIds = listOf(1L)
            ).isEmpty()
        )

        assertTrue(
            selectProducts(
                listOf(product(1, "A")),
                recommendedIds = listOf(99L),
                excludedIds = emptyList()
            ).isEmpty()
        )
    }

    @Test
    fun `inputs are not modified and calls are independent`() {
        val products = mutableListOf(product(1, "A"), product(2, "B"))
        val recommendedIds = mutableListOf(2L, 1L, 2L)
        val excludedIds = mutableListOf(1L)

        val productsCopy = products.toList()
        val recommendedCopy = recommendedIds.toList()
        val excludedCopy = excludedIds.toList()

        val first = selectProducts(products, recommendedIds, excludedIds)
        val second = selectProducts(products, recommendedIds, excludedIds)

        assertEquals(listOf(product(2, "B")), first)
        assertEquals(first, second)
        assertTrue(first !== second, "каждый вызов должен возвращать новый список")

        assertEquals(productsCopy, products)
        assertEquals(recommendedCopy, recommendedIds)
        assertEquals(excludedCopy, excludedIds)
    }

    @Test
    fun `result keeps original product objects with all fields`() {
        val a = product(1, "A")
        val b = product(2, "B", available = false)

        val result = selectProducts(
            listOf(a, b),
            recommendedIds = listOf(1L, 2L),
            excludedIds = emptyList()
        )

        assertEquals(1, result.size)
        assertSame(a, result[0], "должен вернуться исходный объект Product")
        assertEquals(1L, result[0].id)
        assertEquals("A", result[0].name)
        assertTrue(result[0].available)
    }
}
