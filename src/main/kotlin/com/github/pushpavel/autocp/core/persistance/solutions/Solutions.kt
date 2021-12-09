package com.github.pushpavel.autocp.core.persistance.solutions

import com.github.pushpavel.autocp.core.persistance.base.MapWithEventFlow
import com.github.pushpavel.autocp.core.persistance.storage.Storable
import com.google.gson.JsonObject
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


@Service
class Solutions(project: Project) : MapWithEventFlow<String, Solution>(), Storable {

    fun onKey(key: String) = events.filter { it.keys.contains(key) }.map { it.map[key] }

    fun put(value: Solution) = put(value.pathString, value)
    override val id = "solutions"

    override fun load(data: JsonObject) {
        clear()
        data.entrySet().forEach { map[it.key] = Solution.fromJson(it.value.asJsonObject) }
    }

    override fun save(): JsonObject {
        val json = JsonObject()
        forEach { (key, value) -> json.add(key, value.toJson()) }
        return json
    }
}