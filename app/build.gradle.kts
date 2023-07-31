plugins {
    id("com.android.application")
    kotlin("kapt")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.deepak.currencyexchangeapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.deepak.currencyexchangeapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.deepak.currencyexchangeapp.CurrencyExchangeAppTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",

                )
        )
    }
}

dependencies {
    val runTimeKtx = "2.6.1"
    val hilt = "2.44"
    val retrofit = "2.9.0"
    val gson = "2.3.1"
    val composeBom = platform("androidx.compose:compose-bom:2023.06.01")
    val roomVersion = "2.5.2"
    val appcompatVersion = "1.7.0-alpha03"
    val okhttp = "4.9.3"
    val mockk = "1.13.5"
    val coroutinesVersion = "1.7.3"
    val dataStore = "1.0.0-alpha07"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$runTimeKtx")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // implementation("androidx.appcompat:appcompat:$appcompatVersion")
    //Coroutin
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    //Hilt
    implementation("com.google.dagger:hilt-android:$hilt")
    kapt("com.google.dagger:hilt-android-compiler:$hilt")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hilt")
    // ...with Kotlin.
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hilt")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    //Compose
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    //Room
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    testImplementation("androidx.room:room-testing:$roomVersion")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")
    implementation("com.google.code.gson:gson:$gson")
    implementation("com.squareup.okhttp3:okhttp-bom:$okhttp")
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    //Data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //Mokk
    testImplementation("io.mockk:mockk:$mockk")
    androidTestImplementation("io.mockk:mockk:$mockk")
}