plugins {
  `java-library`
  `maven-publish`
}

group = "vottega"
version = "1.0.0"

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

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