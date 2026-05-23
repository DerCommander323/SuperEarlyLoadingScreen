plugins {
    id("java")
    id("application")
}

group = "dev.dercommander"
version = "1.0-SNAPSHOT"


var lwjglVersion = "3.4.1"
var lwjglNatives = "natives-linux"

application {
    mainClass = "dev.dercommander.sels.Main"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-sdl")
    implementation("org.lwjgl:lwjgl::$lwjglNatives")
    implementation("org.lwjgl:lwjgl-sdl::$lwjglNatives")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.dercommander.sels.Main";
        attributes["Premain-Class"] = "dev.dercommander.sels.Main";
    }
}

tasks.test {
    useJUnitPlatform()
}
