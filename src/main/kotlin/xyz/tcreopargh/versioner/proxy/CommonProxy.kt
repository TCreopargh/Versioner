package xyz.tcreopargh.versioner.proxy

import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import xyz.tcreopargh.versioner.Versioner
import xyz.tcreopargh.versioner.config.enableVersionChecking
import xyz.tcreopargh.versioner.config.versionDataURL
import xyz.tcreopargh.versioner.data.VersionData
import xyz.tcreopargh.versioner.util.NetworkHandler
import xyz.tcreopargh.versioner.util.saveVariables
import java.io.IOException

open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent?) {
        Versioner.logger = event?.modLog
    }

    open fun init(event: FMLInitializationEvent?) {
        if (enableVersionChecking) {
            Thread {
                try {
                    Versioner.logger?.info("Starting to fetch version data from $versionDataURL")
                    val jsonString = NetworkHandler.readToString(versionDataURL)
                    val jsonObj = JsonParser().parse(jsonString).asJsonObject
                    Versioner.versionData = VersionData(jsonObj)
                    Versioner.logger?.info("Successfully fetched version data: ")
                    Versioner.logger?.info(Versioner.versionData.toString())
                } catch (e: JsonSyntaxException) {
                    Versioner.logger?.error(e.message, e)
                    Versioner.logger?.error(
                        "Version data JSON syntax error!"
                    )
                } catch (e: IOException) {
                    Versioner.logger?.error(e.message, e)
                    Versioner.logger?.error(
                        "Failed to fetch version data. Check your network connection," +
                            " if you believe it's not your problem, report this to the modpack author."
                    )
                }
            }.apply {
                name = "Versioner Network Thread"
            }.start()
            saveVariables()
        }
    }

    open fun postInit(event: FMLPostInitializationEvent?) {

    }

    open fun i18nSafe(key: String, vararg objects: Any): String {
        return key
    }
}
