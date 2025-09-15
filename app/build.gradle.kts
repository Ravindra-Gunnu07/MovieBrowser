plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.moviebrowser"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.moviebrowser"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // TMDB API Key (accessible with BuildConfig.TMDB_API_KEY)
        buildConfigField("String", "TMDB_API_KEY", "\"${project.properties["TMDB_API_KEY"]}\"")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {


        // Existing ones (already in your project)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.graphics)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.compose.material3)
    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")

        // ✅ New ones (needed for Movie Browser App)
        implementation("androidx.navigation:navigation-compose:2.8.8") // Navigation
        implementation("com.squareup.retrofit2:retrofit:2.9.0")        // Retrofit
        implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Gson
        implementation("io.coil-kt:coil-compose:2.2.2")               // Coil
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Coroutines

        // Testing (already in your project)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.compose.ui.test.junit4)
        debugImplementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.test.manifest)
    }

