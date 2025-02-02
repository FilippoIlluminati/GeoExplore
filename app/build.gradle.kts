plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.geoexplore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-web") // Abilita il server web
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Supporto per JPA
    implementation("org.springframework.boot:spring-boot-starter-security") // Abilita Spring Security

    // Database
    implementation("com.h2database:h2") // H2 database

    // Altre dipendenze
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.openjfx:javafx-controls:23.0.1")
    implementation("org.openjfx:javafx-fxml:23.0.1")
    implementation("org.openjfx:javafx-web:23.0.1")
    implementation("org.json:json:20210307")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito") // Evita conflitti con Mockito vecchio
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

application {
    mainClass.set("Geoexplore.GeoExploreApplication")
}

tasks.test {
    useJUnitPlatform() // Assicura che i test JUnit 5 vengano riconosciuti
}
