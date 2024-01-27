plugins {
    id("java")
}

group = "io.revxrsal"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    implementation("net.java.dev.jna:jna-platform:5.14.0")
    implementation("net.java.dev.jna:jna:5.14.0")
}