plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.paparazzi)
    id("maven-publish")
}

android {
    namespace = "org.mindtec.uikit"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.coil.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.libphonenumber.android)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.exifinterface)
}

// Publicación local del artefacto "release" (AAR + sources jar).
// JitPack usa esto para saber qué componente empaquetar cuando alguien
// hace implementation("com.github.<usuario>:mfui-components:<tag>").
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "org.mindtec.uikit"
                artifactId = "uikit"
                version = "0.2.0"
            }
        }
    }
}
