plugins {
    `kotlin-dsl`
}

group = "com.hcpark.convention"

gradlePlugin {
    plugins {
        register("DetektConventionPlugin") {
            id = libs.plugins.convention.detekt.get().pluginId
            implementationClass = "DetektConventionPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.android.tools.build.gradle)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.detekt.gradle.plugin)
}
