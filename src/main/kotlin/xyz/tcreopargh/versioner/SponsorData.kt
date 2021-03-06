package xyz.tcreopargh.versioner

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import net.minecraft.util.text.*

data class SponsorData(val jsonArray: JsonArray) {

    var categoryList: SponsorList = ArrayList()

    init {
        for (entry in jsonArray) {
            val arrayElement: JsonElement = entry
            if (entry.isJsonObject) {
                val category: SponsorCategory = SponsorCategory(entry.asJsonObject)
                if (!category.isBad()) {
                    categoryList.add(category)
                }
            }
        }
    }

    fun getFormattedText(): MutableList<ITextComponent> {
        val delimiter: ITextComponent = TextComponentString(DELIMITER).setStyle(Style().apply {
            color = TextFormatting.YELLOW
        })
        var components: MutableList<ITextComponent> = mutableListOf(delimiter.br())
        components.add(TextComponentTranslation("versioner.variables.sponsors").setStyle(Style().apply {
            color = TextFormatting.LIGHT_PURPLE
            bold = true
        }).br())
        for (category in categoryList) {
            components.addAll(category.getFormattedText())
        }
        val msg = Versioner.versionData?.sponsorMessage
        if (msg != null) {
            components.add(getTextComponentFromJSON(msg).br())
        }
        components.add(delimiter)
        return components
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
