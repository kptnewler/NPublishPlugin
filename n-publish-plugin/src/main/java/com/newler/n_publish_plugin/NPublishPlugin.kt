package com.newler.n_publish_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class NPublishPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            plugins.apply("maven-publish")


        }
    }
}