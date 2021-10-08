plugins {
    kotlin("jvm") version "1.5.0"
    `maven-publish`
    jacoco
}

group = "com.github.sylhare"
version = "2.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) { exclude("") }
        })
    )
}

tasks.withType<Wrapper> {
    gradleVersion = "7.1"
    distributionType = Wrapper.DistributionType.ALL
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sylhare/RandomK")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
