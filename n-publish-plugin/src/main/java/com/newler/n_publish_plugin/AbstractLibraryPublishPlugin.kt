package com.newler.n_publish_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git

abstract class AbstractLibraryPublishPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        val repo = target.git()
        val git = GitProject(repo)
        target.run {
            afterEvaluate {
                configureDependencies()

                publishing {
                    publications {
                        createMavenPublications(this) {
                            pom {
                                scm {
                                    connection.set("scm:git:${git.url}")
                                    developerConnection.set("scm:git:${git.url}")
                                    url.set(git.url)
                                }

                                licenses {
                                    license {
                                        name.set(target.l)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    abstract fun Project.createMavenPublications(
        publications: PublicationContainer,
        config: MavenPublication.() -> Unit
    )

    open fun Project.configureDependencies() {
        configureDokka()
    }
}