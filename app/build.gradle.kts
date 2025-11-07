import com.android.build.api.dsl.VariantDimension
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
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

        buildConfigStringField(field = ConfigField.API_KEY, propertyKey = "API_KEY")
        buildConfigStringField(field = ConfigField.API_HOST, propertyKey = "API_HOST")
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

detekt {
    buildUponDefaultConfig = true
    allRules = false
    autoCorrect = true
    config.setFrom(files("$rootDir/detekt.yml"))
}

dependencies {
    detektPlugins(libs.detekt.formatting)

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

enum class ConfigField {
    API_KEY, API_HOST
}

fun VariantDimension.buildConfigStringField(field: ConfigField, propertyKey: String) {
    val propertyValue = localProperties.getProperty(propertyKey, "")
    buildConfigField("String", field.name, "\"$propertyValue\"")
}
