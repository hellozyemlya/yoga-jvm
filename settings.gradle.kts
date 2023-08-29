/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
  }
}

buildscript {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
  }
  dependencies {
    classpath("fr.inria.gforge.spoon:spoon-core:10.2.0")
  }
}

include(":java-jvm")
