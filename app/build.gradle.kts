plugins {
    id("java")
    id("application")
}

group = "org.geoexplore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.9")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    // Dipendenze JavaFX
    implementation("org.openjfx:javafx-controls:23.0.1")
    implementation("org.openjfx:javafx-fxml:23.0.1")
    implementation("org.openjfx:javafx-web:23.0.1")  // Aggiungi il modulo javafx-web
}

application {
    mainClass.set("org.geoexplore.App")
}


tasks.test {
    useJUnitPlatform()
}

// Configura il modulo JavaFX
java {
    modularity.inferModulePath.set(true)
}

tasks.withType<JavaExec> {
    jvmArgs = listOf(
            "--module-path", "C:/Programmi/javafx-sdk-23.0.1/lib",
            "--add-modules", "javafx.controls,javafx.fxml,javafx.web"
    )
}

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf(
            "--module-path", "C:/Programmi/javafx-sdk-23.0.1/lib",
            "--add-modules", "javafx.controls,javafx.fxml,javafx.web"
    )
}

