package xyz.tcreopargh.versioner

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.ArrayList
import java.util.LinkedHashMap

data class ChangelogData(val jsonObj: JsonObject) {

    var map: ChangelogMap = LinkedHashMap()

    init{
        for ((key, value) in jsonObj.entrySet()) {
            val arrayElement: JsonElement = value
            val array: JsonArray = if (arrayElement is JsonArray) arrayElement.asJsonArray else JsonArray()
            val versionChangelog: MutableList<String> = ArrayList()
            for (versionChangelogLine in array) {
                versionChangelog.add(versionChangelogLine.asString)
            }
            map[key] = versionChangelog
        }
    }

    operator fun get(category: String): List<String?>? = map[category]

    override fun toString(): String {
        val builder = StringBuilder()
        for ((key, value) in map.entries) {
            builder.append(key).append(":\n")
            for (line in value) {
                builder.append(changelogPrefix).append(line).append("\n")
            }
        }
        if (builder.isNotEmpty()) {
            builder.deleteCharAt(builder.length - 1)
        }
        return builder.toString()
    }
}
