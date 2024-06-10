package com.ant.popular.movies

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get() = project.extensions.getByType<VersionCatalogsExtension>().named("libs")
