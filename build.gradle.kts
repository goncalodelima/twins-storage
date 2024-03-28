plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "com.twins"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    compileOnly("com.github.PlaceholderAPI:PlaceholderAPI:2.10.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.black_ixx:playerpoints:3.2.6");
    compileOnly("com.github.MilkBowl:VaultAPI:1.7");
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("me.devnatan:inventory-framework-platform-bukkit:3.0.8")
    implementation("org.jetbrains:annotations:23.0.0")

    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

bukkit {
    name = "twins-storage"
    prefix = "twins-storage"
    version = "${project.version}"
    depend = listOf("twins-database")
    main = "com.twins.storage.StoragePlugin"
    commands {
        register("storage"){
            aliases = listOf("armazem", "armazens", "storages")
        }
    }
}