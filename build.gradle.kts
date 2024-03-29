plugins {
    id("java")
    `maven-publish`
}

group = "revxrsal"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    implementation("net.java.dev.jna:jna-platform:5.14.0")
    implementation("net.java.dev.jna:jna:5.14.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}