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
    // Dipendenze per la tua applicazione, aggiungine altre se necessario
    implementation("com.google.code.gson:gson:2.8.9")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

application {
    mainClass.set("org.geoexplore.App")  // Specifica la tua classe principale
}

tasks.test {
    useJUnitPlatform()
}