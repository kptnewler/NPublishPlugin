package com.newler.n_publish_plugin

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.credentials.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.internal.impldep.org.eclipse.jgit.lib.Repository
import org.gradle.internal.impldep.org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.repositories
import org.gradle.plugins.signing.SigningExtension
import java.io.File

fun Project.configSonatype() {

}

val Project.versionString: String
    get() = version.toString().takeIf { it != Project.DEFAULT_VERSION && it.isNotBlank() }
        ?: parent?.versionString
        ?: throw GradleException("unspecified project version")

val Project.groupString: String
    get() = group.toString().takeIf(String::isNotBlank)?.takeUnless {
        it == rootProject.name || it.startsWith("${rootProject.name}.")
    } ?: parent?.groupString ?: throw GradleException("unspecified project group")


val Project.useSonatype: Boolean
    get() {
        val OSSRH_USERNAME: String? by vars
        val OSSRH_PASSWORD: String? by vars
        val OSSRH_PACKAGE_GROUP: String? by vars
        return OSSRH_USERNAME != null
                && OSSRH_PASSWORD != null
                && OSSRH_PACKAGE_GROUP != null
                && hasSigningProperties
    }

val Project.hasSigningProperties
    get() = arrayOf("signing.keyId", "signing.password", "signing.secretKeyRingFile").none {
        findProperty(it) != null
    }

fun Project.git(): Repository =
    FileRepositoryBuilder().setGitDir(File(rootDir, ".git")).findGitDir().build()

val Project.license: License?
    get() = rootDir.listFiles()?.asSequence()?.mapNotNull { License.of(it) }?.firstOrNull()

fun Project.publishing(
    config: PublishingExtension.() -> Unit
) = extensions.configure(PublishingExtension::class, config)

fun Project.signing(
    config: SigningExtension.() -> Unit
) = extensions.configure(SigningExtension::class.java, config)

fun Project.configSigning() {
    if (!useSonatype) return

    if (!hasSigningProperties) throw GradleException("signing properties are required")

    plugins.apply("signing")
    afterEvaluate {
        publishing {
            signing {
                // 对发布的产物进行加密 ,比如对Maven pom 和 artifacts 加密
                sign(publications)
            }
        }
    }
}

fun Project.configSonatypeRepository() {
    if (!useSonatype) return
    val ossrhUsername by vars
    val ossrhPassword by vars
    repositories {
        maven {
            url = uri(this.sonatypeRepositoryUrl(project.version.toString()))
            credentials(PasswordCredentials::class.java) {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}

fun Project.configureDokka() {
    extensions.findByName("kotlin")?.let {
        plugins.run {
            apply("org.jetbrains.dokka")
        }
    }
}


inline val Project.vars
    get() = ProjectVariableDelegate.get(this)