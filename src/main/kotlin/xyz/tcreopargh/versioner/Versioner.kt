package xyz.tcreopargh.versioner

import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkCheckHandler
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.Logger
import java.io.IOException

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
    var isUpdateDialogShown = false

    var logger: Logger? = null

    @NetworkCheckHandler
    fun checkModList(versions: Map<String?, String?>?, side: Side?): Boolean {
        return true
    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        Thread {
            try {
                val jsonString = NetworkHandler.readToString(versionDataURL)
                val jsonObj = JsonParser().parse(jsonString).asJsonObject
                versionData = VersionData(jsonObj)
                logger?.info("Successfully fetched version data: ")
                logger?.info(versionData.toString())
            } catch(e: JsonSyntaxException) {
                logger?.error(e.message, e)
                logger?.error(
                    "Version data JSON syntax error!"
                )
            } catch (e: IOException) {
                logger?.error(e.message, e)
                logger?.error(
                    "Failed to fetch version data. Check your network connection," +
                        " if you believe it's not your problem, report this to the modpack author."
                )
            }
        }.apply {
            name = "Versioner Network Thread"
        }.start()
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {

    }
}
