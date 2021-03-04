package xyz.tcreopargh.versioner

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.*

typealias ChangelogMap = MutableMap<String?, List<String?>>?

class VersionData {
    var versionName: String? = null
    var versionFormat: String? = null
    var changelogs: ChangelogMap = null
    var versionCode = -1
    var sponsors: List<String> = ArrayList()
    var versionJsonObject: JsonObject? = null

    fun getEntryString(key: String): String {
        return when (key) {
            "versionName" -> versionName.toString()
            "versionFormat" -> versionFormat.toString()
            "versionCode" -> versionCode.toString()
            "sponsors" -> sponsorsText
            "changelogs" -> changelogsText
            else ->
                if (versionJsonObject?.get(key)?.isJsonNull != false)
                    "null"
                else versionJsonObject?.get(key)?.toString().toString()
        }
    }

    fun getFormattedVersionName(format: String): String {
        var formattedString = format
        val possibleKeys = keySet()
        possibleKeys.addAll(
            listOf(
                "versionName",
                "versionFormat",
                "versionCode",
                "sponsors",
                "changelogs"
            )
        )
        for (key in keySet()) {
            formattedString = formattedString.replace("%$key%", getEntryString(key), false)
        }
        return format
    }

    fun keySet(): MutableSet<String> {
        val keySet: MutableSet<String> = HashSet()
        versionJsonObject!!.entrySet().forEach { e: Map.Entry<String, JsonElement?> -> keySet.add(e.key) }
        return keySet
    }

    val sponsorsText: String
        get() = java.lang.String.join("\n", sponsors)
    val changelogsText: String
        get() {
            var a = null;
            val builder = StringBuilder()
            for ((key, value) in changelogs!!) {
                builder.append(key).append(":\n")
                for (line in value) {
                    builder.append(ConfigHandler.changelogSeparator).append(line).append("\n")
                }
            }
            if (builder.isNotEmpty()) {
                builder.deleteCharAt(builder.length - 1)
            }
            return builder.toString()
        }

    fun isUpdateAvailable(): Boolean {
        return when {
            versionCode > 0 -> ConfigHandler.currentVersion.versionCode > versionCode
            versionName == null -> false
            else -> return Util.compareVersionNames(ConfigHandler.currentVersion.versionName, versionName!!) < 0
        }
    }
}
