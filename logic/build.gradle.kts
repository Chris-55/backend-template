dependencies {
    api(project(":database"))

    implementation("com.auth0:java-jwt:${DependencyVersion.jwt}")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
}