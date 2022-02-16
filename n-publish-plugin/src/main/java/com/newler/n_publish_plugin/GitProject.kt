package com.newler.n_publish_plugin

import com.sun.tools.javac.util.Pair
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git
import org.gradle.internal.impldep.org.eclipse.jgit.lib.Repository

class GitProject(repo: Repository) {
    val git: Git = Git(repo)
    val localName = System.getProperty("user.name")
    val devEmail = repo.config.getString("user", null, "email")
    // 如果devName 为空，则去邮箱@前的账号
    val devName = repo.config.getString("user", null, "name") ?: devEmail?.substringBefore("@")
    val devAccount = setOf(devEmail to devName)
    val url = repo.config.getString("remote", "origin", "url")

    val developers = (try {
      git.log().call().map {
          val author = it.authorIdent
          author.emailAddress to author.name
      }.toSet()
    } catch (e: Throwable) {
        emptySet()
    }).takeIf {it.isNotEmpty()} ?: devAccount.takeIf{devName.isNullOrEmpty()} ?: setOf("${localName}@local" to localName)
// 参考上传 https://central.sonatype.org/publish/publish-gradle/#metadata-definition-and-upload

}