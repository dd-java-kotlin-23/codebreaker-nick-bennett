plugins {
    alias(libs.plugins.android.application)
//    alias(libs.plugins.hilt)
//    alias(libs.plugins.navigation.safeargs)
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

}