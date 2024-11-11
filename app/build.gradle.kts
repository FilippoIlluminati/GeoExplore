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

    // JavaFX dependencies
    implementation("org.openjfx:javafx-controls:23.0.1")
    implementation("org.openjfx:javafx-fxml:23.0.1")
}

application {
    mainClass.set("org.geoexplore.App")
}

tasks.test {
    useJUnitPlatform()
}

// Configure JavaFX runtime options
java {
    modularity.inferModulePath.set(true)
}

tasks.withType<JavaExec> {
    // Percorso aggiornato per JavaFX
    jvmArgs = listOf(
            "--module-path", "C:/Programmi/javafx-sdk-23.0.1/lib",
            "--add-modules", "javafx.controls,javafx.fxml"
    )
}

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf(
            "--module-path", "C:/Programmi/javafx-sdk-23.0.1/lib",
            "--add-modules", "javafx.controls,javafx.fxml"
    )
}
