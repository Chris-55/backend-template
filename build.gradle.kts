import io.gitlab.arturbosch.detekt.Detekt
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    base
    kotlin("jvm") version DependencyVersion.kotlin
    id("org.springframework.boot") version DependencyVersion.spring apply false
    id("io.spring.dependency-management") version DependencyVersion.springDependencyManagementPlugin
    id("io.gitlab.arturbosch.detekt") version DependencyVersion.detekt
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "com.backend.template"
    version = "0.0.1-SNAPSHOT"

    // override spring-managed version
    extra["kotlin.version"] = DependencyVersion.kotlin

    apply(plugin = "org.jetbrains.kotlin.jvm")
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    apply(plugin = "io.spring.dependency-management")
    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(SpringBootPlugin.BOM_COORDINATES)
        }
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        buildUponDefaultConfig = true
        autoCorrect = System.getenv("DETEKT_AUTOCORRECT") != null
    }
    tasks.withType<Detekt>().configureEach {
        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
        }
    }


    tasks.getByName("test", Test::class) {
        useJUnitPlatform()
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${DependencyVersion.detekt}")

        api(kotlin("stdlib-jdk8"))
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("org.slf4j:slf4j-api")
    }
}