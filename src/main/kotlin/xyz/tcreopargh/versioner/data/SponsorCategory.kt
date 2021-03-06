package xyz.tcreopargh.versioner.data;

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentString
import xyz.tcreopargh.versioner.config.changelogPrefix
import xyz.tcreopargh.versioner.util.addTextFormat
import xyz.tcreopargh.versioner.util.br
import xyz.tcreopargh.versioner.util.plus
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author TCreopargh
 */
data class SponsorCategory
constructor(
    val name: String,
    var titleStyle: Style = Style(),
    var sponsorStyle: Style = Style(),
    var sponsors: MutableList<Sponsor> = ArrayList()
) {
    constructor(jsonObj: JsonObject) : this(jsonObj.get("category").asString ?: "") {
        val titleFormatsList: JsonArray? = jsonObj.getAsJsonArray("titleFormat")
        val sponsorFormatsList: JsonArray? = jsonObj.getAsJsonArray("sponsorFormat")
        val sponsorsList: JsonArray? = jsonObj.getAsJsonArray("sponsors")
        if (titleFormatsList != null) {
            for (element in titleFormatsList) {
                val format = element?.asString
                if (format != null) {
                    titleStyle.addTextFormat(format)
                }
            }
        }
        if (sponsorFormatsList != null) {
            for (element in sponsorFormatsList) {
                val format = element?.asString
                if (format != null) {
                    sponsorStyle.addTextFormat(format)
                }
            }
        }
        if (sponsorsList != null) {
            for (element in sponsorsList) {
                if (element.isJsonObject) {
                    val name: String? = element.asJsonObject?.get("name")?.asString
                    val uuid: String? = element.asJsonObject?.get("uuid")?.asString
                    val javaUuid: UUID? = if (uuid != null) UUID.fromString(uuid) else null
                    sponsors.add(Sponsor(name, javaUuid))
                } else {
                    val str: String? = element?.asString
                    try {
                        val uuid = UUID.fromString(str)
                        sponsors.add(Sponsor(uuid = uuid))
                    } catch (e: IllegalArgumentException) {
                        sponsors.add(Sponsor(name = str))
                    }
                }
            }
        }
    }

    fun isBad(): Boolean {
        return name == ""
    }

    fun addSponsor(sponsor: Sponsor) {
        sponsors.add(sponsor)
    }

    fun removeSponsor(sponsor: Sponsor) {
        sponsors.remove(sponsor)
    }

    operator fun contains(sponsor: Sponsor): Boolean {
        return sponsors.contains(sponsor)
    }

    operator fun contains(player: EntityPlayer): Boolean {
        for (sponsor in sponsors) {
            if (sponsor.matchesPlayer(player)) {
                return true
            }
        }
        return false
    }

    fun getFormattedName(): ITextComponent = TextComponentString(name).setStyle(titleStyle)

    /**
     * Using list because a large ITextComponent object can produce StackOverflowError
     */
    fun getFormattedText(): MutableList<ITextComponent> {
        val components: MutableList<ITextComponent> =
            mutableListOf(TextComponentString(name).setStyle(titleStyle) + TextComponentString(":"))
        for (sponsor in sponsors) {
            components.add(TextComponentString(changelogPrefix) + sponsor.getFormattedText(sponsorStyle).br())
        }
        return components
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(name).append(":\n")
        for (sponsor in sponsors) {
            builder.append(changelogPrefix).append(sponsor.toString())
        }
        return builder.toString()
    }
}
