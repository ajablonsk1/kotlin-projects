plugins {
    application
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":stub"))
    runtimeOnly("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")

    testImplementation(kotlin("test-junit"))
    testImplementation("io.grpc:grpc-testing:${rootProject.ext["grpcVersion"]}")
}

tasks.register<JavaExec>("HelloWorldServer") {
    dependsOn("classes")
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.grpc.examples.helloworld.HelloWorldServerKt")
}

tasks.register<JavaExec>("HelloWorldServer1") {
    dependsOn("classes")
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.grpc.examples.helloworld.HelloWorldServer1Kt")
}

tasks.register<JavaExec>("HelloWorldServer2") {
    dependsOn("classes")
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.grpc.examples.helloworld.HelloWorldServer2Kt")
}

val helloWorldServerStartScripts = tasks.register<CreateStartScripts>("helloWorldServerStartScripts") {
    mainClass.set("io.grpc.examples.helloworld.HelloWorldServerKt")
    applicationName = "hello-world-server"
    outputDir = tasks.named<CreateStartScripts>("startScripts").get().outputDir
    classpath = tasks.named<CreateStartScripts>("startScripts").get().classpath
}

val helloWorldServerStartScripts1 = tasks.register<CreateStartScripts>("helloWorldServerStartScripts1") {
    mainClass.set("io.grpc.examples.helloworld.HelloWorldServer1Kt")
    applicationName = "hello-world-server1"
    outputDir = tasks.named<CreateStartScripts>("startScripts").get().outputDir
    classpath = tasks.named<CreateStartScripts>("startScripts").get().classpath
}

val helloWorldServerStartScripts2 = tasks.register<CreateStartScripts>("helloWorldServerStartScripts2") {
    mainClass.set("io.grpc.examples.helloworld.HelloWorldServer2Kt")
    applicationName = "hello-world-server2"
    outputDir = tasks.named<CreateStartScripts>("startScripts").get().outputDir
    classpath = tasks.named<CreateStartScripts>("startScripts").get().classpath
}

tasks.named("startScripts") {
    dependsOn(helloWorldServerStartScripts)
    dependsOn(helloWorldServerStartScripts1)
    dependsOn(helloWorldServerStartScripts2)
}

tasks.withType<Test> {
    useJUnit()

    testLogging {
        events = setOf(org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED, org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED, org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED)
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }
}
