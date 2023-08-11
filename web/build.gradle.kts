import org.springframework.boot.gradle.tasks.bundling.BootJar

logger.warn("Running on Java version ${System.getProperty("java.version")}")

plugins {
    id("org.springframework.boot")
}

dependencies {
    api(project(":logic"))

    implementation("com.auth0:java-jwt:${DependencyVersion.jwt}")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.getByName<BootJar>("bootJar") {
    archiveClassifier.set("boot")
}

// Configuration for bootJar and bootRun tasks
springBoot {
    mainClass.set("com.backend.template.BackendTemplateApplicationKt")
}