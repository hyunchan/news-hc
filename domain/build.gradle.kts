plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.convention.detekt)
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(project(":util"))

    implementation(libs.hilt.core)
    implementation(libs.napier)
    implementation(libs.androidx.paging3.common)

    ksp(libs.hilt.compiler)

    testImplementation(libs.bundles.test)
}
