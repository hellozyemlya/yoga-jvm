import spoon.Launcher
import spoon.processing.AbstractProcessor
import spoon.reflect.code.CtInvocation
import java.nio.file.Paths


plugins {
  id("java")
  id("maven-publish")
}

group = "com.facebook.yoga"
version = "0.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  mavenLocal()
}

val sourceDir = Paths.get("${project.projectDir}/../java").normalize()
val stubsDir = "${project.projectDir}/src/stubs/java"



sourceSets {

  main {
    java {
      srcDir("${sourceDir}/com")
      srcDir("${sourceDir}/java/gen")
      srcDir("${project.buildDir}/spoon/generated/java")
      exclude {
        it.file.name == "YogaNative.java" && it.file.toPath().startsWith(sourceDir)
      }
    }
  }
}

dependencies {
  implementation("com.google.code.findbugs:jsr305:3.0.2")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "${project.group}"
      artifactId = project.name
      version = "${project.version}"

      from(components["java"])
    }
  }
}


tasks {
  val spoonTask = register("injectLibraryLoader") {
    doLast {
      val spoon = Launcher()
      spoon.environment.apply {
        noClasspath = true
        isAutoImports = false
      }

      spoon.addInputResource("${project.projectDir}/../java/com/facebook/yoga/YogaNative.java")
      spoon.addInputResource(stubsDir)
      spoon.addInputResource("${project.projectDir}/src/main/java")

      spoon.setSourceOutputDirectory("${project.buildDir}/spoon/generated/java")

      spoon.setOutputFilter("com.facebook.yoga.YogaNative")

      spoon.addProcessor(object: AbstractProcessor<CtInvocation<Any>>() {
        override fun process(element: CtInvocation<Any>) {
          if(element.referencedTypes.any { it.qualifiedName == "com.facebook.soloader.SoLoader" }) {
            val type =spoon.factory.Type().get<Unit>("hellozyemlya.loader.LibLoader")
            val typeAccess = factory.createTypeAccess<Unit>(type.reference)
            val loadLibrary = type.getMethodsByName("load")[0].reference
            val invocation = spoon.factory.createInvocation(typeAccess, loadLibrary, factory.createLiteral("yoga"))
            element.replace(invocation)
          }
        }
      })

      spoon.run()
    }
  }

  named("compileJava").get().dependsOn(spoonTask)
}
