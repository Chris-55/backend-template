plugins {
    id("nu.studer.jooq") version DependencyVersion.jooqEtienneStuderPlugin
    id("org.flywaydb.flyway") version DependencyVersion.flywayPlugin
}

dependencies {
    runtimeOnly("org.postgresql:postgresql:${DependencyVersion.postgres}")


    api("org.jooq:jooq:${DependencyVersion.jooq}")
    implementation("org.jooq:jooq-codegen:${DependencyVersion.jooq}")
    // jooq needs to have this specified explicitly
    jooqGenerator("org.postgresql:postgresql:${DependencyVersion.postgres}")
    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:${DependencyVersion.jakartaXmlBindApi}")
}

flyway {
    url = System.getProperty("POSTGRES_URL") ?: "jdbc:postgresql://localhost:5432/backendtemplate"
    user = System.getProperty("POSTGRES_USERNAME") ?: "postgres"
    password = System.getProperty("POSTGRES_PASSWORD") ?: "admin"
    schemas = arrayOf("public")
}

jooq {
    version.set(DependencyVersion.jooq)  //  set jooq version

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = System.getProperty("POSTGRES_DRIVER") ?: "org.postgresql.Driver"
                    url = System.getProperty("POSTGRES_URL") ?: "jdbc:postgresql://localhost:5432/backendtemplate"
                    user = System.getProperty("POSTGRES_USERNAME") ?: "postgres"
                    password = System.getProperty("POSTGRES_PASSWORD") ?: "admin"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate.apply {
                        withPojos(true)
                        withDaos(true)
                    }

                    target.apply {
                        packageName = "com.backend.template.dbschema.jooqGenerated"
                        directory = "./src/generated/kotlin"
                    }

                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}