package xyz.tcreopargh.versioner

import com.google.gson.JsonParser
import net.minecraft.util.text.*
import net.minecraft.util.text.event.ClickEvent

typealias ChangelogMap = MutableMap<String, List<String>>
typealias SponsorList = MutableList<SponsorCategory>

const val DELIMITER = "=============================="

operator fun ITextComponent.plus(t: ITextComponent): ITextComponent = this.appendSibling(t)
operator fun ITextComponent.plus(s: String): ITextComponent = this.appendText(s)
fun ITextComponent.br(): ITextComponent = this.appendText("\n")

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

/*
@Deprecated("This should not be used")
fun showUpdateDialog() {
    Thread {
        val stamp: Long = System.currentTimeMillis()
        while (true) {
            Thread.sleep(500)
            if (System.currentTimeMillis() - stamp > 1000 * 20) {
                break
            }
            if (Versioner.versionData?.isReady == true) {
                val options = arrayOf(
                    I18n.format("versioner.update_dialog_option.yes"),
                    I18n.format("versioner.update_dialog_option.no")
                )
                val input = JOptionPane.showOptionDialog(
                    null,
                    I18n.format(
                        "versioner.update_dialog_message",
                        modpackName,
                        Versioner.versionData?.versionName,
                        currentVersion.versionName
                    ),
                    I18n.format("versioner.update_dialog_title"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]
                )
                if (input == 0) {
                    if (Desktop.isDesktopSupported()) {
                        val desktop = Desktop.getDesktop()
                        try {
                            desktop.browse(URI(updateURL))
                        } catch (ignored: IOException) {
                        } catch (ignored: URISyntaxException) {
                        }
                    }
                }
                break
            }
        }
    }.apply {
        name = "Versioner Update Dialog Thread"
    }.start()
}
*/

fun getUpdateChatMessage(): List<ITextComponent> {
    val delimiter: ITextComponent = TextComponentString(DELIMITER).setStyle(
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
            currentVersion.versionName
        ).setStyle(Style().apply {
            color = TextFormatting.YELLOW
        })

    val diff: Int? = Versioner.versionData?.getVersionDiff()

    var latestVersion: ITextComponent = i18n(
        "versioner.variables.latest_version"
    ).setStyle(Style().apply {
        color = TextFormatting.DARK_GREEN
    }) + TextComponentString(" ") +
        TextComponentString(
            Versioner.versionData?.versionName ?: "N/A"
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
    val changelogList: List<String?>? = Versioner.versionData?.getCurrentChangelogs()
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


    val updateLink: ITextComponent = i18n(
        "versioner.variables.update_link"
    ).setStyle(
        Style().apply {
            color = TextFormatting.AQUA
            underlined = true
            clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, updateURL)
        }
    )

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

