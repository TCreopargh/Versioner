package xyz.tcreopargh.versioner.events

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import xyz.tcreopargh.versioner.Versioner
import xyz.tcreopargh.versioner.Versioner.versionData
import xyz.tcreopargh.versioner.config.versionNotifications
import xyz.tcreopargh.versioner.util.getTextComponentFromJSON
import xyz.tcreopargh.versioner.util.getUpdateChatMessage
import java.util.*

/**
 * @author TCreopargh
 */
@EventBusSubscriber(modid = Versioner.MOD_ID)
object EventHandler {

    var recognizedPlayers: MutableList<UUID> = ArrayList()

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onPlayerLogin(event: EntityJoinWorldEvent) {
        if (event.entity is EntityPlayer && event.world.isRemote) {
            val player = event.entity as EntityPlayer
            if (player.uniqueID == Minecraft.getMinecraft().player.uniqueID) {
                if (!Versioner.isUpdateMessageShown
                    && versionNotifications.showLoginChatUpdateNotification
                    && versionData?.isUpdateAvailable() == true
                ) {
                    for (msg in getUpdateChatMessage()) {
                        player.sendMessage(msg)
                    }
                    Versioner.isUpdateMessageShown = true
                }
                recognizedPlayers.add(player.uniqueID)
                val msg = versionData?.welcomeMessage
                if (msg != null) {
                    player.sendMessage(getTextComponentFromJSON(msg))
                }
            }
        }
    }


    @SubscribeEvent
    fun onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent) {
        if (eventArgs.modID == Versioner.MOD_ID) {
            ConfigManager.sync(Versioner.MOD_ID, Config.Type.INSTANCE)
        }
    }
}
