package xyz.tcreopargh.versioner

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.*
import kotlin.collections.ArrayList

data class SponsorData(val jsonArray: JsonArray) {

    var categoryList: SponsorList = ArrayList()

    init {
        for (entry in jsonArray) {
            val arrayElement: JsonElement = entry
            if(entry.isJsonObject) {
                val category: SponsorCategory = SponsorCategory(entry.asJsonObject)
                if(!category.isBad()) {
                    categoryList.add(category)
                }
            }
        }
    }

    operator fun get(categoryName: String): SponsorCategory? {
        for (category in categoryList) {
            if(category.name == categoryName) {
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
