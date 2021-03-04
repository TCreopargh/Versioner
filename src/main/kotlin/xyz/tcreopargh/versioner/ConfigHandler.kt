package xyz.tcreopargh.versioner

import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.Config.LangKey
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@EventBusSubscriber(modid = Versioner.MOD_ID)
@Config(modid = "versioner")
object ConfigHandler {
    @Config.Comment("Extra text added before each line of changelog")
    var changelogSeparator = " - "

    @LangKey("versioner.config.current_version_category")
    var currentVersion: CurrentVersion = CurrentVersion()

    @Config.Comment("Modpack name to use when the version data JSON does not contain a modpack name")
    var modpackName = ""

    @Config.Comment("Where the version data JSON will be fetched from, KEEP http:// or https://")
    var versionDataURL = ""

    @Config.Comment("The url that is opened when the user clicks on update, KEEP http:// or https://")
    var updateURL = ""

    @SubscribeEvent
    fun onConfigChanged(eventArgs: OnConfigChangedEvent) {
        if (eventArgs.modID == Versioner.MOD_ID) {
            ConfigManager.sync(Versioner.MOD_ID, Config.Type.INSTANCE)
        }
    }

    class CurrentVersion {
        @Config.Comment("Version name of the current version")
        var versionName = "1.0.0"

        @Config.Comment("Version code of the current version")
        var versionCode = 0

        @Config.Comment("How to output formatted version name. Only used for displaying current version.")
        var versionFormat = "%versionName%"
    }
}
