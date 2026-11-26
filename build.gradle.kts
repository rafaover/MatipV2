// Plugins are declared here once so they are not loaded again in each module's classloader.
plugins {
    alias(libs.plugins.android.gradle.application) apply false
    alias(libs.plugins.android.multiplatform.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.roomDb) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.secrets) apply false
}
