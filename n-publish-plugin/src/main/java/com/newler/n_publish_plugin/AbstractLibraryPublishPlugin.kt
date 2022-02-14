package com.newler.n_publish_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git

abstract class AbstractLibraryPublishPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        val repo = target.git()
        val git = Git(repo)
        repo.config
    }
}