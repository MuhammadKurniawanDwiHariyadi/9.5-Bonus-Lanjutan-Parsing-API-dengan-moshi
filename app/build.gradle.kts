plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    // untuk parsing API moshi
    id("kotlin-kapt")
}

android {
    namespace = "com.dicoding.myworkmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.myworkmanager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "APP_ID", "\"5ed81e836d82a9ae006a7fc84c16cfe4\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.work:work-runtime:2.8.1")
    implementation("com.loopj.android:android-async-http:1.4.10")

    // untuk parsing api moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.8.0")
}