package ch.ttt.demo_maven

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoMavenApplication

fun main(args: Array<String>) {
	runApplication<DemoMavenApplication>(*args)
}
