package xyz.tcreopargh.versioner

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import java.util.*

data class VersionData(val versionJsonObject: JsonObject, var doInitialize: Boolean = true) {
    var versionName: String? = null
    var versionFormat: String? = null
    var changelogs: ChangelogMap = null
    var versionCode = -1
    var sponsors: List<String> = ArrayList()
    var isReady: Boolean = false
    var variables: JsonObject? = null

    @Deprecated("Use ready() instead", replaceWith = ReplaceWith("ready()"))

    fun VersionData?.ready(): Boolean {
        return this?.isReady ?: false
    }

    fun getEntryString(key: String): String {
        if (!isReady) return ""
        return when (key) {
            "versionName" -> versionName.toString()
            "versionFormat" -> versionFormat.toString()
            "versionCode" -> versionCode.toString()
            "sponsors" -> sponsorsText
            "changelogs" -> changelogsText
            else ->
                if (variables?.get(key)?.isJsonNull != false)
                    "null"
                else variables?.get(key)?.toString().toString()
        }
    }

    @Throws(MalformedJsonException::class, JsonSyntaxException::class)
    fun initialize() {
        val jsonObj = versionJsonObject
        if (jsonObj.has("versionName")) {
            val versionName = jsonObj["versionName"].asString
            this.versionName = versionName
        }
        if (jsonObj.has("versionCode")) {
            val versionCode = jsonObj["versionCode"].asInt
            this.versionCode = versionCode
        }
        if (jsonObj.has("versionFormat")) {
            val versionFormat = jsonObj["versionFormat"].asString
            this.versionFormat = versionFormat
        }
        if (jsonObj.has("changelogs")) {
            val changelogs: ChangelogMap = LinkedHashMap()
            val changelogsObj = jsonObj["changelogs"].asJsonObject
            for ((key, value) in changelogsObj.entrySet()) {
                val array = value.asJsonObject.getAsJsonArray(key)
                val versionChangelog: MutableList<String> = ArrayList()
                for (versionChangelogLine in array) {
                    versionChangelog.add(versionChangelogLine.asString)
                }
                changelogs?.set(key, versionChangelog)
            }
            this.changelogs = changelogs
        }
        if (jsonObj.has("variables")) {
            this.variables = jsonObj["variables"].asJsonObject
        }
        this.isReady = true
    }

    init {
        if (doInitialize) {
            initialize()
        }
    }

    fun getFormattedVersionName(format: String): String {
        var formattedString = format
        val possibleKeys = variableNamesSet()
        possibleKeys.addAll(
            listOf(
                "versionName",
                "versionFormat",
                "versionCode",
                "sponsors",
                "changelogs"
            )
        )
        for (key in possibleKeys) {
            formattedString = formattedString.replace("%$key%", getEntryString(key), false)
        }
        return format
    }

    fun variableNamesSet(): MutableSet<String> {
        val keySet: MutableSet<String> = HashSet()
        variables?.entrySet()?.forEach { e: Map.Entry<String, JsonElement?> -> keySet.add(e.key) }
        return keySet
    }

    val sponsorsText: String
        get() = java.lang.String.join("\n", sponsors)
    val changelogsText: String
        get() {
            val builder = StringBuilder()
            for ((key, value) in changelogs!!) {
                builder.append(key).append(":\n")
                for (line in value) {
                    builder.append(changelogSeparator).append(line).append("\n")
                }
            }
            if (builder.isNotEmpty()) {
                builder.deleteCharAt(builder.length - 1)
            }
            return builder.toString()
        }

    fun isUpdateAvailable(): Boolean {
        return when {
            versionCode > 0 -> currentVersion.versionCode > versionCode
            versionName == null -> false
            else -> return compareVersionNames(currentVersion.versionName, versionName!!) < 0
        }
    }

    override fun toString(): String {
        return versionJsonObject.toString()
    }
}
