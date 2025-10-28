plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
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

    ksp(libs.hilt.compiler)
}
