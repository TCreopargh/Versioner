package xyz.tcreopargh.versioner.data

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import xyz.tcreopargh.versioner.util.jsonStringArrayToList

data class MainMenuData(val jsonObj: JsonObject) {
    var text: List<String>? = null
    var tooltipText: List<String>? = null
    var clickLink: String? = null

    init {
        if (jsonObj.has("text")) {
            val arr: JsonArray? = jsonObj.get("text")?.asJsonArray
            text = jsonStringArrayToList(arr)
        }
        if (jsonObj.has("tooltipText")) {
            val arr: JsonArray? = jsonObj.get("tooltipText")?.asJsonArray
            tooltipText = jsonStringArrayToList(arr)
        }
        if (jsonObj.has("clickLink")) {
            clickLink = jsonObj.get("clickLink")?.asString
        }
    }
}
