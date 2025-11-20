import com.android.build.api.dsl.VariantDimension
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.convention.detekt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.hcpark.news.app"
    compileSdk = properties["compileSdk"].toString().toInt()

    defaultConfig {
        applicationId = "com.hcpark.news.app"
        minSdk = properties["minSdk"].toString().toInt()
        targetSdk = properties["targetSdk"].toString().toInt()
        versionCode = properties["versionCode"].toString().toInt()
        versionName = properties["versionName"].toString()

        val localProperties = localProperties()

        buildConfigStringField(BuildConfigField.API_KEY, localProperties)
        buildConfigStringField(BuildConfigField.API_HOST, localProperties)
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))
    implementation(project(":util"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.android)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.napier)

    ksp(libs.hilt.compiler)
}

enum class BuildConfigField {
    API_KEY, API_HOST
}


fun localProperties() = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

fun VariantDimension.buildConfigStringField(field: BuildConfigField, properties: Properties) {
    val key = field.name
    val propertyValue = properties.getOrDefault(key, "")
    buildConfigField("String", key, "\"$propertyValue\"")
}

