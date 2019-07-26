import java.net.URI
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Base64
import javax.crypto.SecretKey

group = "com.petpool"
version = "1.0-SNAPSHOT"

data class AppVersion(val major: Int, val minor: Int, val iter: Int){
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
    id("org.springframework.boot").version("2.1.6.RELEASE")
    `maven-publish`
    id("org.sonarqube").version("2.7")
}


buildscript{
    repositories {
        mavenCentral()
    }
    dependencies{
        classpath ("io.jsonwebtoken","jjwt-api", "0.10.7")
        classpath ("io.jsonwebtoken","jjwt-impl", "0.10.7")
        classpath ("io.jsonwebtoken","jjwt-jackson", "0.10.7")
    }
}


apply(plugin = "io.spring.dependency-management")

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
    testCompile ("org.mockito","mockito-core", DependencyVersions.mockitoVersion)
    testCompile ("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.projectlombok","lombok", DependencyVersions.lombokVersion)
    annotationProcessor("org.projectlombok","lombok", DependencyVersions.lombokVersion)

    implementation("ch.qos.logback", "logback-classic", DependencyVersions.logbackVersion)
    implementation("ch.qos.logback", "logback-core", DependencyVersions.logbackVersion)
    implementation("org.slf4j", "slf4j-api", DependencyVersions.slf4jVersion)
    implementation("javax.annotation","javax.annotation-api", DependencyVersions.javaxAnnotationVersion)
    implementation("io.projectreactor", "reactor-core", DependencyVersions.projectReactorVersion)

    implementation ("org.hibernate","hibernate-core",DependencyVersions.hibernateVersion)
    implementation ("org.hibernate","hibernate-entitymanager",DependencyVersions.hibernateVersion)

    implementation ("mysql","mysql-connector-java",DependencyVersions.jdbcDriverVersion)
    implementation ("org.apache.commons","commons-lang3",DependencyVersions.commonLangVersion)
    implementation ("org.apache.commons","commons-collections4",DependencyVersions.collectionUtilsVersion)
    implementation ("com.google.guava","guava",DependencyVersions.guavaVersion)
    implementation ("com.fasterxml.jackson.core","jackson-databind",DependencyVersions.jacksonVersion)

    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-tomcat")
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    
    implementation ("org.springframework","spring-orm",DependencyVersions.ormVersion)
    implementation("org.apache.commons","commons-dbcp2",DependencyVersions.connectionPoolVersion)

    implementation ("io.jsonwebtoken","jjwt-api", DependencyVersions.jwtVersion)
    implementation ("io.jsonwebtoken","jjwt-impl", DependencyVersions.jwtVersion)
    implementation ("io.jsonwebtoken","jjwt-jackson", DependencyVersions.jwtVersion)

}

configure<JavaPluginConvention> {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

}

application {
    mainClassName="com.petpool.Application"
}


tasks.jar{
    manifest {
        attributes(
                "Main-Class" to mainClass,
                "Class-Path" to "lib/",
                "Version" to appVersion.toString()
        )
    }
}
tasks.compileJava{
    this.options.encoding ="UTF-8"
}

val azureArtifactsGradleAccessToken: String by project

publishing  {
    publications {
        create<MavenPublication>("PetPool") {
            from(components["java"])
            version = appVersion.toString()
            artifactId = "petpool"
        }
    }

        repositories{
        maven {
            url = URI("https://pkgs.dev.azure.com/perfo-foundation/_packaging/petpool/maven/v1")
            credentials {
                username = "AZURE_ARTIFACTS"
                password = System.getenv("AZURE_ARTIFACTS_ENV_ACCESS_TOKEN") ?: azureArtifactsGradleAccessToken
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
}
