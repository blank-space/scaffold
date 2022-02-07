plugins {
    kotlin("jvm") version "1.5.31"
}



repositories {
    jcenter()
    google()
    mavenCentral()
}


dependencies {
    implementation("com.android.tools.build:gradle-api:4.2.2")
    implementation(kotlin("stdlib"))
    gradleApi()
}


