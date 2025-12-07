import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

group = "me.jeremy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.google.code.gson:gson:2.10")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

val deletePreviousGitHook by tasks.registering(Delete::class) {
    group = "utils"
    description = "Deleting previous githook"

    val preCommit = "${rootProject.rootDir}/.git/hooks/pre-commit"
    if (file(preCommit).exists()) {
        delete(preCommit)
    }
}

val installGitHook by tasks.registering(Copy::class) {
    group = "utils"
    description = "Adding githook to local working copy, this must be run manually"

    dependsOn(deletePreviousGitHook)
    from("${rootProject.rootDir}/pre-commit")
    into("${rootProject.rootDir}/.git/hooks")
    fileMode = 0b111101101
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    disabledRules.set(setOf("no-wildcard-imports"))
}
