import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

@Suppress("unused")
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "io.gitlab.arturbosch.detekt")

            extensions.configure<DetektExtension> {
                buildUponDefaultConfig = true
                allRules = false
                autoCorrect = true
                config.setFrom(files("${rootProject.projectDir}/detekt.yml"))
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("detektPlugins", libs.findLibrary("detekt-formatting").get())
            }

            tasks.withType<Detekt>().configureEach {
                reports {
                    html.required.set(true)
                    html.outputLocation.set(
                        rootProject.layout.buildDirectory.file("reports/detekt/${project.name}-detekt.html")
                    )
                }

                jvmTarget = "21"

                doLast {
                    logger.lifecycle("Detekt report: ${reports.html.outputLocation.get().asFile}")
                }
            }
        }
    }
}