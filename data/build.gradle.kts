plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.convention.detekt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.hcpark.news.data"
    compileSdk = properties["compileSdk"].toString().toInt()

    defaultConfig {
        minSdk = properties["minSdk"].toString().toInt()
        testInstrumentationRunner = properties["testInstrumentationRunner"].toString()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":util"))

    implementation(libs.hilt.android)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.room)
    implementation(libs.napier)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging3.runtime)

    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)

    testImplementation(libs.bundles.test)
}
