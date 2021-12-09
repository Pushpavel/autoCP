package com.github.pushpavel.autocp.core.persistance.solutions

import com.google.gson.JsonObject

data class Solution(
    val displayName: String,
    val pathString: String,
    val timeLimit: Int = 1000,
) {
    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("displayName", displayName)
        json.addProperty("pathString", pathString)
        json.addProperty("timeLimit", timeLimit)
        return json
    }

    companion object {
        fun fromJson(json: JsonObject): Solution {
            return Solution(
                displayName = json.get("displayName").asString,
                pathString = json.get("pathString").asString,
                timeLimit = json.get("timeLimit").asInt
            )
        }
    }
}