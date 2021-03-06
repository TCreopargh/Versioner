package xyz.tcreopargh.versioner

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkCheckHandler
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.Logger
import xyz.tcreopargh.versioner.data.VersionData
import xyz.tcreopargh.versioner.proxy.CommonProxy

/**
 * @author TCreopargh
 */
@Mod(
    modid = Versioner.MOD_ID,
    name = Versioner.MOD_NAME,
    version = Versioner.VERSION,
    modLanguageAdapter = Versioner.LANG_ADAPTER,
    dependencies = Versioner.DEPENDENCIES
)
object Versioner {
    const val MOD_ID = "versioner"
    const val MOD_NAME = "Versioner"
    const val VERSION = "1.0.0"
    const val LANG_ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"
    const val DEPENDENCIES = "required-after:crafttweaker;required-after:forgelin"

    const val CLIENT_PROXY = "xyz.tcreopargh.versioner.proxy.ClientProxy"
    const val COMMON_PROXY = "xyz.tcreopargh.versioner.proxy.CommonProxy"

    var versionData: VersionData? = null
    var isUpdateMessageShown = false

    var logger: Logger? = null

    @SidedProxy(
        clientSide = CLIENT_PROXY,
        serverSide = COMMON_PROXY
    )
    @JvmField
    var proxy: CommonProxy? = null

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
        proxy?.preInit(event)
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        proxy?.init(event)
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy?.postInit(event)
    }
}
