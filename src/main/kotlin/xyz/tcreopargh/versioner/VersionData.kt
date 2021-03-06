package xyz.tcreopargh.versioner

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import org.apache.logging.log4j.Level
import java.io.IOException
import java.util.*

data class VersionData(val jsonObj: JsonObject, var doInitialize: Boolean = true) {
    var versionName: String? = null
    var versionFormat: String? = null
    var changelogs: ChangelogData? = null
    var versionCode = -1
    var sponsors: SponsorData? = null
    var isReady: Boolean = false
    var variables: JsonObject? = null
    var updateLink: String? = null
    var welcomeMessage: String? = null
    var sponsorMessage: String? = null

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
            "sponsors" -> sponsors.toString()
            "changelogs" -> changelogs.toString()
            "updateLink" -> updateLink.toString()
            "welcomeMessage" -> welcomeMessage.toString()
            "sponsorMessage" -> sponsorMessage.toString()
            else ->
                if (variables?.get(key)?.isJsonNull != false)
                    "null"
                else variables?.get(key)?.toString().toString()
        }
    }

    @Throws(MalformedJsonException::class, JsonSyntaxException::class, java.lang.IllegalStateException::class)
    fun initialize() {
        if (jsonObj.has("versionName")) {
            val versionName = jsonObj["versionName"]?.asString
            this.versionName = versionName
        }
        if (jsonObj.has("versionCode")) {
            val versionCode = jsonObj["versionCode"]?.asInt
            this.versionCode = versionCode ?: -1
        }
        if (jsonObj.has("versionFormat")) {
            val versionFormat = jsonObj["versionFormat"]?.asString
            this.versionFormat = versionFormat
        }
        if (jsonObj.has("updateLink")) {
            val updateLink = jsonObj["updateLink"]?.asString
            this.updateLink = updateLink
        }
        if (jsonObj.has("welcomeMessage")) {
            val welcomeMessage = jsonObj["welcomeMessage"].toString()
            this.welcomeMessage = welcomeMessage
        }
        if (jsonObj.has("sponsorMessage")) {
            val sponsorMessage = jsonObj["sponsorMessage"].toString()
            this.sponsorMessage = sponsorMessage
        }
        if (jsonObj.has("changelogs")) {
            val obj = jsonObj["changelogs"]?.asJsonObject
            if (obj != null) {
                this.changelogs = ChangelogData(obj)
            }
        }
        if (jsonObj.has("sponsors")) {
            val arr = jsonObj["sponsors"]?.asJsonArray
            if (arr != null) {
                this.sponsors = SponsorData(arr)
            }
        }
        if (jsonObj.has("variables")) {
            this.variables = jsonObj["variables"].asJsonObject
        }
        this.isReady = true
    }

    init {
        if (doInitialize) {
            try {
                initialize()
            } catch (e: Exception) {
                when (e) {
                    is IOException, is MalformedJsonException, is IllegalStateException -> {
                        Versioner.logger?.log(Level.ERROR, e)
                        Versioner.logger?.log(Level.ERROR, "Failed to initialize version data object.")
                    }
                    else -> throw e
                }
            }
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
                "changelogs",
                "updateLink",
                "welcomeMessage",
                "sponsorMessage"
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

    fun isUpdateAvailable(): Boolean = when {
        versionCode >= 0 -> this.versionCode > currentVersion.versionCode
        versionName == null -> false
        else -> compareVersionNames(this.versionName, currentVersion.versionName) < 0
    }

    fun getVersionDiff(): Int? = if (this.versionCode >= 0) {
        this.versionCode - currentVersion.versionCode
    } else {
        null
    }

    fun getCurrentChangelogs(): List<String?>? {
        val version = this.versionName
        return if (version != null) this.changelogs?.get(version) else null
    }


    override fun toString(): String {
        return jsonObj.toString()
    }
}
