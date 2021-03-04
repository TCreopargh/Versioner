package xyz.tcreopargh.versioner

import com.google.gson.JsonParser
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.io.IOException
import java.util.*

@Mod(
    modid = Versioner.MOD_ID,
    name = Versioner.MOD_NAME,
    version = Versioner.VERSION,
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter"
)
object Versioner {
    const val MOD_ID = "versioner"
    const val MOD_NAME = "Versioner"
    const val VERSION = "1.0.0"

    var versionData: VersionData? = null

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    fun preinit(event: FMLPreInitializationEvent) {

    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        versionData = VersionData()
        try {
            val jsonString = NetworkHandler.readToString(ConfigHandler.versionDataURL)
            val jsonObj = JsonParser().parse(jsonString).asJsonObject
            versionData?.versionJsonObject = jsonObj
            if (jsonObj.has("versionName")) {
                val versionName = jsonObj["versionName"].asString
                versionData?.versionName = versionName
            }
            if (jsonObj.has("versionCode")) {
                val versionCode = jsonObj["versionCode"].asInt
                versionData?.versionCode = versionCode
            }
            if (jsonObj.has("versionFormat")) {
                val versionFormat = jsonObj["versionFormat"].asString
                versionData?.versionFormat = versionFormat
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
                versionData?.changelogs = changelogs
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    fun postinit(event: FMLPostInitializationEvent) {

    }
}
