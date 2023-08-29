buildscript {
  repositories {
    mavenCentral() // or any other repository where Spoon is available
  }
  dependencies {
    classpath("fr.inria.gforge.spoon:spoon-core:10.2.0")
  }
}
rootProject.name = "yoga-jvm"

