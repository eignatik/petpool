import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.boot.gradle.tasks.run.BootRun
import java.net.URI
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
import kotlin.collections.HashMap

group = "com.petpool"
version = "1.0-SNAPSHOT"

data class AppVersion(val major: Int, val minor: Int, val iter: Int) {
    override fun toString(): String {
        return "$major.$minor.$iter"
    }
}

val mainClass = "com.petpool.Application"
val appVersion = AppVersion(major = 0, minor = 0, iter = 1)
val javaVersion = JavaVersion.VERSION_11
val encoding = "UTF-8"

object DependencyVersions {
    const val jwtVersion = "0.10.7"
    const val hibernateVersion = "5.4.3.Final"
    const val jdbcDriverVersion = "8.0.16"
    const val commonLangVersion = "3.8.1"
    const val collectionUtilsVersion = "4.3"
    const val guavaVersion = "19.0"
    const val jacksonVersion = "2.9.9"
    const val mockitoVersion = "2.4.2"
    const val testNGVersion = "6.8"
    const val logbackVersion = "1.2.3"
    const val lombokVersion = "1.18.8"
    const val slf4jVersion = "1.7.26"
    const val javaxAnnotationVersion = "1.3.2"
    const val projectReactorVersion = "3.2.10.RELEASE"
    const val springBootVersion = "2.1.6.RELEASE"
    const val ormVersion = "5.1.8.RELEASE"
    const val connectionPoolVersion = "2.6.0"
}

plugins {
    java
    id("application")
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.springframework.boot").version("2.1.6.RELEASE")
    `maven-publish`
    id("org.sonarqube").version("2.7")
    jacoco

}



buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.jsonwebtoken", "jjwt-api", "0.10.7")
        classpath("io.jsonwebtoken", "jjwt-impl", "0.10.7")
        classpath("io.jsonwebtoken", "jjwt-jackson", "0.10.7")
    }
}



repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}


dependencies {
    testCompile("org.testng", "testng", DependencyVersions.testNGVersion)
    testCompile("org.mockito", "mockito-core", DependencyVersions.mockitoVersion)
    testCompile("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.projectlombok", "lombok", DependencyVersions.lombokVersion)
    annotationProcessor("org.projectlombok", "lombok", DependencyVersions.lombokVersion)

    implementation("ch.qos.logback", "logback-classic", DependencyVersions.logbackVersion)
    implementation("ch.qos.logback", "logback-core", DependencyVersions.logbackVersion)
    implementation("org.slf4j", "slf4j-api", DependencyVersions.slf4jVersion)
    implementation("javax.annotation", "javax.annotation-api", DependencyVersions.javaxAnnotationVersion)
    implementation("io.projectreactor", "reactor-core", DependencyVersions.projectReactorVersion)

    implementation("org.hibernate", "hibernate-core", DependencyVersions.hibernateVersion)
    implementation("org.hibernate", "hibernate-entitymanager", DependencyVersions.hibernateVersion)

    implementation("mysql", "mysql-connector-java", DependencyVersions.jdbcDriverVersion)
    implementation("org.apache.commons", "commons-lang3", DependencyVersions.commonLangVersion)
    implementation("org.apache.commons", "commons-collections4", DependencyVersions.collectionUtilsVersion)
    implementation("com.google.guava", "guava", DependencyVersions.guavaVersion)
    implementation("com.fasterxml.jackson.core", "jackson-databind", DependencyVersions.jacksonVersion)

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-tomcat")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework", "spring-orm", DependencyVersions.ormVersion)
    implementation("org.apache.commons", "commons-dbcp2", DependencyVersions.connectionPoolVersion)

    implementation("io.jsonwebtoken", "jjwt-api", DependencyVersions.jwtVersion)
    implementation("io.jsonwebtoken", "jjwt-impl", DependencyVersions.jwtVersion)
    implementation("io.jsonwebtoken", "jjwt-jackson", DependencyVersions.jwtVersion)

}

configure<JavaPluginConvention> {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

}



application {
    mainClassName = "com.petpool.Application"
}

tasks.withType(JavaExec::class) {
    if (System.getProperty("encryption.key") != null && System.getProperty("security.encryptionKeyJwt") != null) {
        systemProperties["encryption.key"] = System.getProperty("encryption.key")
        systemProperties["security.encryptionKeyJwt"] = System.getProperty("security.encryptionKeyJwt")
    }

}


tasks.jar {
    manifest {
        attributes(
                "Main-Class" to mainClass,
                "Class-Path" to "lib/",
                "Version" to appVersion.toString()
        )
    }
}
tasks.compileJava {
    this.options.encoding = "UTF-8"
}

val azureArtifactsGradleAccessToken: String by project

publishing {
    publications {
        create<MavenPublication>("PetPool") {
            from(components["java"])
            version = appVersion.toString()
            artifactId = "petpool"
        }
    }

    repositories {
        maven {
            url = URI("https://pkgs.dev.azure.com/perfo-foundation/_packaging/petpool/maven/v1")
            credentials {
                username = "AZURE_ARTIFACTS"
                password = System.getenv("AZURE_ARTIFACTS_ENV_ACCESS_TOKEN")
                        ?: azureArtifactsGradleAccessToken
            }
        }
    }


    tasks.register("generate_jwt_key") {
        doLast {
            val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
            val storedKey = Base64.getEncoder().encodeToString(key.encoded)
            print("JWT-key for config property: $storedKey")
        }
    }

    tasks.register("generate_aes_key") {
        doLast {
            val keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(128)
            val key: SecretKey = keyGen.generateKey()
            val storedKey = Base64.getEncoder().encodeToString(key.encoded)
            print("AES128-key for config property: $storedKey")
        }
    }


    tasks.register("encrypt_aes_string") {
        description = "Encrypt string by AES with key(AES-key). Sent param to task : gradle encrypt_aes_string -Psrc=mystring -Pkey=123"
        doLast {
            if (project.hasProperty("src") || project.hasProperty("key")) {
                val strKey = project.property("key") as String
                val decodedKey = Base64.getDecoder().decode(strKey.toByteArray())
                val key = SecretKeySpec(decodedKey, "AES")

                var encrypted: String
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(ByteArray(16)))
                val src = project.property("src") as String
                val enc = cipher.doFinal(src.toByteArray())
                encrypted = Base64.getEncoder().encodeToString(enc)

                print("Encripted string: $encrypted")
            }
        }
    }


    jacoco {
        toolVersion = "0.8.4"
        reportsDir = file("$buildDir/jacocoReports")

    }

    tasks.jacocoTestReport {
        reports {
            xml.isEnabled = true
            csv.isEnabled = false
            html.destination = file("${buildDir}/report/html")
            xml.destination = file("${buildDir}/report/report.xml")
        }
    }

    tasks.jacocoTestCoverageVerification {
        violationRules {
            rule {
                element = "CLASS"
                limit {
                    minimum = "1.0".toBigDecimal()
                    counter = "LINE"
                    value = "COVEREDRATIO"
                }
                excludes = listOf(
                        "com.petpool.application.exception.*",
                        "com.petpool.domain.model.user.*",
                        "com.petpool.domain.model.user.*",
                        "com.petpool.domain.shared.DataBaseInitializer",
                        "com.petpool.application.constants.*",
                        "com.petpool.application.util.DataBaseProperties",
                        "com.petpool.application.util.LocalDataBaseProperties",
                        "com.petpool.application.util.response.ErrorType",
                        "com.petpool.config.*",
                        "com.petpool.Application")
            }


        }
    }

    tasks.test {
        useTestNG()
        maxHeapSize = "1G"
    }
}
