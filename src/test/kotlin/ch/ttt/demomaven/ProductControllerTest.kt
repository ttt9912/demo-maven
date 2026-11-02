/*
                                * Copyright (c) 2026 ttt. All rights reserved.
                                */
package ch.ttt.demomaven

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.collections.get

class ProductControllerTest {
    private val controller = ProductController()

    @Test
    fun `getAllProducts returns all products`() {
        // Act
        val result = controller.getAllProducts()

        // Assert
        assertEquals(2, result.size)
        assertEquals(Product(1, "Laptop", 1200.0), result[0])
        assertEquals(Product(2, "Phone", 800.0), result[1])
    }

    @Test
    fun `getProductById returns product when it exists`() {
        // Act
        val result = controller.getProductById(1L)

        // Assert
        assertEquals(Product(1, "Laptop", 1200.0), result)
    }

    @Test
    fun `getProductById throws exception when product does not exist`() {
        // Act and Assert
        assertThrows(NoSuchElementException::class.java) {
            controller.getProductById(999L)
        }
    }
}
