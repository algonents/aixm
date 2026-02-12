plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "com.algonents.aixm"
version = "0.1.0"

// JAXB code generation task
val jaxbGenDir = layout.buildDirectory.dir("generated/sources/jaxb")

val jaxbConfig: Configuration by configurations.creating

dependencies {
    jaxbConfig("org.glassfish.jaxb:jaxb-xjc:4.0.5")
    jaxbConfig("org.glassfish.jaxb:jaxb-runtime:4.0.5")

    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.5")

    testImplementation(libs.kotlin.test.junit)
}

tasks.register<JavaExec>("generateJaxb") {
    classpath = jaxbConfig
    mainClass = "com.sun.tools.xjc.XJCFacade"
    args = listOf(
        "-d", jaxbGenDir.get().asFile.absolutePath,
        "-b", "src/main/resources/aixm-5.1.1/binding.xml",
        "-extension",
        "-nv",
        "src/main/resources/aixm-5.1.1/AIXM_Features.xsd",
        "src/main/resources/aixm-5.1.1/message/AIXM_BasicMessage.xsd"
    )
    doFirst {
        jaxbGenDir.get().asFile.mkdirs()
    }
}

sourceSets {
    main {
        java {
            srcDir(jaxbGenDir)
        }
    }
}

tasks.named("compileJava") {
    dependsOn("generateJaxb")
}

tasks.named("compileKotlin") {
    dependsOn("generateJaxb")
}
