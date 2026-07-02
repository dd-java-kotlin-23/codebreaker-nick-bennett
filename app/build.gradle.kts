import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.ksp)
}

android {

    namespace = project.property("basePackageName") as String
    compileSdk = (project.property("targetSdk") as String).toInt()

    defaultConfig {
        
        applicationId = project.property("basePackageName") as String
        minSdk = (project.property("minSdk") as String).toInt()
        targetSdk = (project.property("targetSdk") as String).toInt()
        versionCode = (project.property("versionCode") as String).toInt()
        versionName = project.property("version") as String
        
        resValue("string", "app_name", project.property("appName") as String)
        
    }
    
    buildTypes {
        release {
            isMinifyEnabled = false
            // TODO: Add ProGuard
        }
    }

    buildFeatures {
        resValues = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
        targetCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
    }

    packaging {
        resources {
            excludes += "/META-INF/NOTICE.md"
            excludes += "/META-INF/LICENSE.md"
        }
    }

}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.valueOf("JVM_${libs.versions.java.get()}")
    }
}

dependencies {
    implementation(project(":client"))
    implementation(project(":services"))

    implementation(libs.app.compat)
    implementation(libs.activity)
    implementation(libs.fragment)

    implementation(libs.constraint.layout)
    implementation(libs.recycler.view)

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    implementation(libs.preference)

    implementation(libs.material)

    implementation(libs.kotlin)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    implementation(libs.hilt.android.core)
    implementation(libs.dagger.core)
    ksp(libs.hilt.compiler)
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

    // TODO: Add testing dependencies.
}