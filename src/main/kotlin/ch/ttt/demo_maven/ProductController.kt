package ch.ttt.demo_maven

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class Product(
    val id: Long,
    val name: String,
    val price: Double
)

@RestController
@RequestMapping("/api/products")
class ProductController {

    private val products = mutableListOf(
        Product(1, "Laptop", 1200.0),
        Product(2, "Phone", 800.0)
    )

    @GetMapping
    fun getAllProducts(): List<Product> {
        return products
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): Product {
        return products.find { it.id == id }
            ?: throw NoSuchElementException("Product with id $id not found")
    }
}