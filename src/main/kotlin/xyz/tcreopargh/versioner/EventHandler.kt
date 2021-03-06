package xyz.tcreopargh.versioner

import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.stream.MalformedJsonException
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Level
import java.util.*


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
                    && Versioner.versionData?.isUpdateAvailable() == true
                ) {
                    for (msg in getUpdateChatMessage()) {
                        player.sendMessage(msg)
                    }
                    Versioner.isUpdateMessageShown = true
                }
                recognizedPlayers.add(player.uniqueID)
                val msg = Versioner.versionData?.welcomeMessage
                if (msg != null) {
                    try {
                        JsonParser().parse(msg)
                        val component = ITextComponent.Serializer.jsonToComponent(msg)
                        if (component != null) {
                            player.sendMessage(component)
                        }
                    } catch (e: JsonParseException) {
                        Versioner.logger?.log(Level.INFO, "Welcome message is not a valid JSON, using fallback to directly display the string instead...")
                        player.sendMessage(TextComponentString(msg))
                    } catch (e: MalformedJsonException) {
                        Versioner.logger?.log(Level.INFO, "Welcome message is not a valid JSON, using fallback to directly display the string instead...")
                        player.sendMessage(TextComponentString(msg))
                    }
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
