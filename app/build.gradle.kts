plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.music_xiehailong"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.music_xiehailong"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("com.github.bumptech.glide:glide:5.0.0-rc01")
    implementation("com.github.anrwatchdog:anrwatchdog:1.4.0")
    implementation("com.squareup.leakcanary:leakcanary-android:3.0-alpha-8")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper:3.0.14")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("org.apache.commons:commons-lang3:3.9")
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation("com.squareup.retrofit2:converter-scalars:2.8.1")
}