package gather.models

import com.google.gson.JsonObject
import com.intellij.util.containers.OrderedSet
import com.github.pushpavel.autocp.database.Problem

data class ProblemJson(
    val name: String,
    val group: String,
    val url: String,
    val interactive: Boolean,
    val memoryLimit: Int,
    val timeLimit: Int,
    val tests: ArrayList<TestJson>,
    val testType: String,
    val input: JsonObject,
    val output: JsonObject,
    val languages: JsonObject,
    val batch: BatchJson
) {

    fun toProblem(): Problem {

        val testcases = tests.mapIndexed { index, testJson ->
            testJson.toTestcase("Testcase #$index")
        }

        val data = JsonObject()

        data.addProperty("interactive", interactive)
        data.addProperty("timeLimit", timeLimit)
        data.addProperty("memoryLimit", memoryLimit)
        data.addProperty("testType", testType)

        data.add("input", input)
        data.add("output", output)
        data.add("languages", languages)

        return Problem(name, group, OrderedSet(testcases), -1, data)
    }

}

