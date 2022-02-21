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
                                description.set(target.description)
                                name.set(target.name)
                                url.set(git.url)
                                scm {
                                    connection.set("scm:git:${git.url}")
                                    developerConnection.set("scm:git:${git.url}")
                                    url.set(git.url)
                                }

                                licenses {
                                    license {
                                        name.set(target.license?.name)
                                        url.set(target.license?.url)
                                    }
                                }

                                developers {
                                    git.developers.forEach {(e, n) ->
                                        developer {
                                            id.set(n)
                                            name.set(n)
                                            email.set(e)
                                        }
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

    fun MavenPublication.configure(project: Project) {
        artifactId = project.name
        groupId = project.groupString
        version = project.versionString
    }
}