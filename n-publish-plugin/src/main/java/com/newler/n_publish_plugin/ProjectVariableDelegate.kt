package com.newler.n_publish_plugin

import org.gradle.api.Project
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KProperty

class ProjectVariableDelegate private constructor(
    private val project: Project
) {
    companion object {
        fun get(project: Project) = ProjectVariableDelegate(project)
    }

    private val cache: MutableMap<String, String?> by lazy {
        ConcurrentHashMap<String, String?>()
    }

    /*
    * 重载 by 操作符
    */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? = cache.getOrPutIfNotNull(property.name) {
        project.findProperty(property.name)?.toString()?.takeIf { it.isNotBlank() }
            ?: System.getenv(property.name)?.takeIf { it.isNotBlank() }
    }
}

/*
* value 不为空直接获取
* value 为空，设置为默认值
*/
private inline fun <K,V> MutableMap<K, V>.getOrPutIfNotNull(key: K, defaultValue: ()->V): V {
    return get(key) ?: defaultValue().apply {
        if (this != null) put(key, this)
    }
}