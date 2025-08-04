plugins {
  kotlin("jvm") version "1.9.24"
  kotlin("plugin.spring") version "1.9.24"
  `java-library`
  `maven-publish`
}

group = "vottega"
version = "1.0.3"

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

dependencies {
  api(platform("org.springframework.boot:spring-boot-dependencies:3.3.2"))

  api("org.springframework.security:spring-security-core")

  compileOnly("org.springframework.security:spring-security-web")
  compileOnly("org.springframework.security:spring-security-config")

  compileOnly("org.springframework:spring-web")
  compileOnly("jakarta.servlet:jakarta.servlet-api")
  compileOnly("org.springframework:spring-webflux")
  compileOnly("io.projectreactor:reactor-core")


  compileOnly("org.springframework.boot:spring-boot-autoconfigure")
  compileOnly("org.springframework.boot:spring-boot")
}

repositories {
  mavenCentral()
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      artifactId = "security-starter"
      pom {
        name.set("Vottega Security Starter")
        description.set("Common security (MVC/WebFlux) auto-configuration")
      }
    }
  }
  repositories {
    maven {
      name = "github"
      url = uri("https://maven.pkg.github.com/vottega/security")
      credentials {
        username = findProperty("gpr.user") as String?
        password = findProperty("gpr.key") as String?
      }
    }
  }
}