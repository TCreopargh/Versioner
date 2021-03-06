package xyz.tcreopargh.versioner.data

import com.google.gson.JsonArray
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.text.*
import xyz.tcreopargh.versioner.Versioner.versionData
import xyz.tcreopargh.versioner.util.DELIMITER
import xyz.tcreopargh.versioner.util.SponsorList
import xyz.tcreopargh.versioner.util.br
import xyz.tcreopargh.versioner.util.getTextComponentFromJSON

/**
 * @author TCreopargh
 * @constructor A JSON array of sponsor categories
 */
data class SponsorData(val jsonArray: JsonArray) {

    var categoryList: SponsorList = ArrayList()

    init {
        for (entry in jsonArray) {
            if (entry.isJsonObject) {
                val category = SponsorCategory(entry.asJsonObject)
                if (!category.isBad()) {
                    categoryList.add(category)
                }
            }
        }
    }

    /**
     * Using list because a large ITextComponent object can produce StackOverflowError
     */
    fun getFormattedText(): MutableList<ITextComponent> {
        val delimiter: ITextComponent = TextComponentString(DELIMITER).setStyle(Style().apply {
            color = TextFormatting.YELLOW
        })
        val components: MutableList<ITextComponent> = mutableListOf(delimiter.br())
        components.add(TextComponentTranslation("versioner.variables.sponsors").setStyle(Style().apply {
            color = TextFormatting.LIGHT_PURPLE
            bold = true
        }).br())
        for (category in categoryList) {
            components.addAll(category.getFormattedText())
        }
        val msg = versionData?.sponsorMessage
        if (msg != null) {
            components.add(getTextComponentFromJSON(msg).br())
        }
        components.add(delimiter)
        return components
    }

    /**
     * Checks what category a player is in
     * @return The category the player is in, null if not found in all categories
     */
    fun checkPlayer(player: EntityPlayer): SponsorCategory? {
        for (category in categoryList) {
            if (player in category) {
                return category
            }
        }
        return null
    }

    operator fun get(categoryName: String): SponsorCategory? {
        for (category in categoryList) {
            if (category.name == categoryName) {
                return category
            }
        }
        return null
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (category in categoryList) {
            builder.append(category.toString())
        }
        return builder.toString()
    }
}
