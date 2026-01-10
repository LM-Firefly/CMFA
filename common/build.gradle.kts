plugins {
    alias(libs.plugins.android.library)
}

dependencies {
    compileOnly(project(":hideapi"))

    implementation(libs.kotlin.coroutine)
    implementation(libs.androidx.core)
}
