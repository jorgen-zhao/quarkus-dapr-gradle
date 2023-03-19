plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkiverse.dapr:quarkus-dapr:1.0.4")
    implementation("com.squareup.okio:okio-jvm:3.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    modules {
        module("com.squareup.okio:okio") {
            replacedBy("com.squareup.okio:okio-jvm", "change it now")
        }
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "com.squareup.okhttp3" && requested.name == "okhttp" && requested.version == "3.14.9") {
                useVersion("4.10.0")
                because("fixes critical bug in 1.2")
            }

        }
    }

}
group = "com.qhx"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
