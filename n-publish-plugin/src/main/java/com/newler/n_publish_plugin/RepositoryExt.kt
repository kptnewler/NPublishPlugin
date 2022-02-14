package com.newler.n_publish_plugin

import org.gradle.api.artifacts.repositories.MavenArtifactRepository

const val SONATYPE_RELEASES_URL = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
const val SONATYPE_SNAPSHOT_URL ="https://s01.oss.sonatype.org/content/repositories/snapshots/"
fun MavenArtifactRepository.sonatypeRepositoryUrl(version: String) =
    if (version.endsWith("SNAPSHOT")) SONATYPE_SNAPSHOT_URL else SONATYPE_RELEASES_URL