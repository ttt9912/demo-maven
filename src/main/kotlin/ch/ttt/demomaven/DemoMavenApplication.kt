/* Copyright (c) 2026 ttt. All rights reserved. */
package ch.ttt.demomaven

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoMavenApplication

@Suppress("SpreadOperator") // avoid build failure `detekt-check`
fun main(args: Array<String>) {
    runApplication<DemoMavenApplication>(*args)
}
