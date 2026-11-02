/*
                                * Copyright (c) 2026 ttt. All rights reserved.
                                */
package ch.ttt.demomaven

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIT
    @Autowired
    constructor(
        private val mockMvc: MockMvc,
    ) {
        @Test
        fun `GET products returns all products`() {
            mockMvc
                .get("/api/products")
                .andExpect {
                    status { isOk() }
                    jsonPath("$[0].id") { value(1) }
                    jsonPath("$[0].name") { value("Laptop") }
                    jsonPath("$[0].price") { value(1200.0) }
                    jsonPath("$[1].id") { value(2) }
                    jsonPath("$[1].name") { value("Phone") }
                    jsonPath("$[1].price") { value(800.0) }
                }
        }

        @Test
        fun `GET product by id returns the requested product`() {
            mockMvc
                .get("/api/products/1")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.id") { value(1) }
                    jsonPath("$.name") { value("Laptop") }
                    jsonPath("$.price") { value(1200.0) }
                }
        }
    }
