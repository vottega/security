package vottega.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VottegaApplication

fun main(args: Array<String>) {
  runApplication<VottegaApplication>(*args)
}
