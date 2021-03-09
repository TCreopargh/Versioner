/**
 * @author TCreopargh
 */
package xyz.tcreopargh.versioner.util

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import crafttweaker.CraftTweakerAPI
import crafttweaker.api.data.*
import crafttweaker.api.minecraft.CraftTweakerMC
import net.minecraft.nbt.JsonToNBT
import net.minecraft.nbt.NBTException
import net.minecraft.server.management.PlayerList
import net.minecraft.util.text.*
import net.minecraft.util.text.event.ClickEvent
import net.minecraft.util.text.event.HoverEvent
import xyz.tcreopargh.versioner.Versioner
import xyz.tcreopargh.versioner.Versioner.versionData
import xyz.tcreopargh.versioner.commands.CommandHandler
import xyz.tcreopargh.versioner.config.*
import xyz.tcreopargh.versioner.data.SponsorCategory
import kotlin.math.floor

typealias ChangelogMap = MutableMap<String, List<String>>
typealias SponsorList = MutableList<SponsorCategory>

const val CT_NAMESPACE = "mods.versioner."

val currentVariables: MutableMap<String, IData?> = HashMap()

operator fun ITextComponent.plus(t: ITextComponent): ITextComponent = this.appendSibling(t)
operator fun ITextComponent.plus(s: String): ITextComponent = this.appendText(s)
fun ITextComponent.br(): ITextComponent = this.appendText("\n")
fun PlayerList.getAllPlayerNames(): List<String> {
    val list: MutableList<String> = ArrayList()
    players.forEach { v ->
        list.add(v.name)
    }
    return list
}

fun i18n(s: String, vararg o: Any): ITextComponent =
    TextComponentTranslation(s, o)

fun compareVersionNames(a: String?, b: String?): Int {
    if (a == null || b == null) {
        return 0
    }
    val separator = Regex("[^\\d]+")
    val arrayA = a.split(separator)
    val arrayB = b.split(separator)
    var i = 0
    while (i < arrayA.size || i < arrayB.size) {
        val seqA = arrayA[i]
        val seqB = arrayB[i]
        if (seqA.toIntOrNull() != null && seqB.toIntOrNull() != null) {
            when {
                (seqA.toInt() > seqB.toInt()) ->
                    return 1
                (seqA.toInt() < seqB.toInt()) ->
                    return -1
            }
        }
        i++
    }
    return 0
}

fun getCurrentEntryString(key: String): String {
    return when (key) {
        "currentVersionName" -> currentVersion.versionName
        "currentVersionCode" -> currentVersion.versionCode.toString()
        "isUpdateAvailable" -> "§c" + i18nSafe("versioner.variables.update_available.fail")
        else -> currentVariables[key]?.toString() ?: ""
    }
}

/**
 * I18n that simply returns the lang key on dedicated servers
 */
fun i18nSafe(key: String, vararg objects: Any): String {
    return Versioner.proxy?.i18nSafe(key, *objects) ?: key
}

fun saveVariables() {
    for (entry in currentVersion.variables) {
        var key: String? = null
        var value: String? = null
        for (i in entry.indices) {
            if (entry[i] == '=' && i < entry.length - 1) {
                key = entry.substring(0 until i)
                value = entry.substring(i + 1)
            }
        }
        if (key != null && value != null) {
            currentVariables[key] = getDataFromJsonElement(JsonParser().parse(value))
        }
    }
}

fun getFormattedString(format: String): String =
    if (versionData?.isReady == true) {
        versionData?.getFormattedString(format) ?: format
    } else {
        getCurrentFormattedString(format)
    }


fun getCurrentFormattedString(format: String): String {
    var formattedString = format
    val possibleKeys: MutableSet<String> = HashSet(currentVariables.keys)
    possibleKeys.addAll(
        listOf(
            "currentVersionName",
            "currentVersionCode",
            "isUpdateAvailable"
        )
    )
    for (key in possibleKeys) {
        formattedString = formattedString.replace("%$key%", getCurrentEntryString(key), false)
    }
    return formattedString

}

fun getCurrentFormattedVersion(): String = getCurrentFormattedString(currentVersion.versionFormat)

/**
 * @return The update link used for click events
 * 'updateLink' field in version data takes priority over the one in the config
 */
fun getUpdateLink(): String = versionData?.updateLink ?: updateURL

/**
 * Using list because a large ITextComponent object can produce StackOverflowError
 */
fun getUpdateChatMessage(): List<ITextComponent> {
    val delimiter: ITextComponent = TextComponentString(delimiter).setStyle(
        Style().apply {
            color = TextFormatting.LIGHT_PURPLE
        }
    )

    val currentVersion: ITextComponent = i18n(
        "versioner.variables.current_version"
    ).setStyle(Style().apply {
        color = TextFormatting.GOLD
    }) + TextComponentString(" ") +
        TextComponentString(
            getCurrentFormattedVersion()
        ).setStyle(Style().apply {
            color = TextFormatting.YELLOW
        })

    val diff: Int? = versionData?.getVersionDiff()

    var latestVersion: ITextComponent = i18n(
        "versioner.variables.latest_version"
    ).setStyle(Style().apply {
        color = TextFormatting.DARK_GREEN
    }) + TextComponentString(" ") +
        TextComponentString(
            versionData?.getFormattedVersionName() ?: "N/A"
        ).setStyle(Style().apply {
            color = TextFormatting.GREEN
        }) + TextComponentString(" ")
    if (diff != null) {
        latestVersion +=
            TextComponentTranslation(
                "versioner.variables.n_versions_newer",
                (diff.toString())
            ).setStyle(Style().apply {
                color = TextFormatting.LIGHT_PURPLE
            })
    }
    var changelogs: ITextComponent = TextComponentString("")
    val changelogList: List<String?>? = versionData?.getCurrentChangelogs()
    if (changelogList != null) {
        changelogs += i18n("versioner.variables.changelogs").setStyle(Style().apply {
            color = TextFormatting.DARK_AQUA
            bold = true
        }).br()
        for (i in changelogList.indices) {
            val entry = changelogList[i]
            if (entry != null) {
                changelogs += TextComponentString(changelogPrefix).setStyle(Style().apply {
                    color = TextFormatting.GOLD
                }) + TextComponentString(entry).setStyle(Style().apply {
                    color = TextFormatting.YELLOW
                })
            }
            if (i < changelogList.size - 1) {
                changelogs.br()
            }
        }
        changelogs.br()
    }


    var updateLink: ITextComponent = i18n(
        "versioner.variables.update_link"
    ).setStyle(
        Style().apply {
            color = TextFormatting.AQUA
            bold = true
            underlined = true
            clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, getUpdateLink())
            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponentTranslation("versioner.variables.update_link_tooltip")
                    .setStyle(Style().apply {
                        color = TextFormatting.YELLOW
                    })
            )
        }
    )

    if (versionData?.sponsors != null) {
        updateLink += TextComponentString("§r    ") + i18n("versioner.variables.sponsors_list").setStyle(Style().apply {
            color = TextFormatting.RED
            bold = true
            underlined = true
            clickEvent = ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/${CommandHandler.SponsorsCommand.NAME} ${CommandHandler.SponsorsCommand.ARG_LIST}"
            )
            hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponentTranslation("versioner.variables.sponsors_list_tooltip")
                    .setStyle(Style().apply {
                        color = TextFormatting.YELLOW
                    })
            )
        })
    }

    return listOf(
        delimiter,
        i18n("versioner.variables.update_chat_message_title")
            .setStyle(Style().apply {
                bold = true
                color = TextFormatting.AQUA
            }),
        currentVersion,
        latestVersion,
        changelogs,
        updateLink,
        delimiter
    )
}

fun Style.addTextFormat(textFormatString: String) {
    val str = textFormatString.toUpperCase()
    if (str == "BOLD") {
        this.bold = true
    } else if (str == "STRIKETHROUGH") {
        this.strikethrough = true
    } else if (str == "UNDERLINE" || str == "UNDERLINED") {
        this.underlined = true
    } else if (str == "OBFUSCATED") {
        this.obfuscated = true
    } else if (str == "ITALIC") {
        this.italic = true
    } else {
        val format = TextFormatting.getValueByName(str)
        if (format != null) {
            this.color = format
        }
    }
}

fun getTextComponentFromJSON(msg: String): ITextComponent {
    try {
        JsonParser().parse(msg)
        val component = ITextComponent.Serializer.jsonToComponent(msg)
        if (component != null) {
            return component
        }
    } catch (ignored: Exception) {
    }
    return TextComponentString(msg)
}

fun jsonToData(json: String?): IData? {
    try {
        val tagCompound = JsonToNBT.getTagFromJson(json ?: "{}")
        return CraftTweakerMC.getIData(tagCompound)
    } catch (e: NBTException) {
        CraftTweakerAPI.logError(e.message, e)
    }
    return null
}

fun getDataFromJsonElement(element: JsonElement?): IData {
    if (element == null) {
        return DataString("null")
    }
    when {
        element.isJsonPrimitive -> {
            val primitive = element.asJsonPrimitive
            when {
                primitive.isBoolean -> return DataBool(element.asBoolean)
                primitive.isNumber  -> {
                    val num: Double = element.asDouble
                    if (num == floor(num)) {
                        return DataInt(num.toInt())
                    }
                    return DataDouble(num)
                }
                primitive.isString  -> return DataString(element.asString ?: "null")
            }
        }
        element.isJsonNull      -> return DataString("null")
        element.isJsonArray     -> {
            val list: MutableList<IData> = ArrayList()
            for (component in element.asJsonArray) {
                list.add(getDataFromJsonElement(component))
            }
            return DataList(list, false)
        }
        element.isJsonObject    -> {
            val map: MutableMap<String, IData> = LinkedHashMap()
            for ((key, value) in element.asJsonObject.entrySet()) {
                map[key] = getDataFromJsonElement(value)
            }
            return DataMap(map, false)
        }
    }
    return DataString(element.asString ?: "null")
}

fun getMainMenuTexts(): List<String> {
    var list: MutableList<String>? = versionData?.mainMenu?.text?.toMutableList()
    if (list == null) {
        list = mainMenu.textLines.asList().toMutableList()
    }
    for (i in list.indices) {
        list[i] = getFormattedString(list[i])
    }
    return list
}

fun getMainMenuTooltipTexts(): List<String> {
    var list: MutableList<String>? = versionData?.mainMenu?.tooltipText?.toMutableList()
    if (list == null) {
        list = mainMenu.tooltipText.asList().toMutableList()
    }
    for (i in list.indices) {
        list[i] = getFormattedString(list[i])
    }
    return list
}

fun getClickLink(): String {
    val link = versionData?.mainMenu?.clickLink ?: mainMenu.clickLink
    return if (link == "") getUpdateLink() else link
}

fun jsonStringArrayToList(array: JsonArray?): List<String> {
    val list: MutableList<String> = ArrayList()
    if (array != null) {
        for (element in array) {
            val str = element.asString
            if (str != null) {
                array.add(str)
            }
        }
    }
    return list
}
