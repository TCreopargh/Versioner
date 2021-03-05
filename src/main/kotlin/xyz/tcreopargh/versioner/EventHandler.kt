package xyz.tcreopargh.versioner

import net.minecraft.client.gui.GuiMainMenu
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@EventBusSubscriber(modid = Versioner.MOD_ID)
object EventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onMainMenu(event: GuiOpenEvent) {
        if (versionNotifications.showMainMenuDialog && event.gui is GuiMainMenu && !Versioner.isUpdateDialogShown && Versioner.versionData?.isUpdateAvailable() == true) {
            showUpdateDialog()
            Versioner.isUpdateDialogShown = true
        }
    }

    @SubscribeEvent
    fun onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent) {
        if (eventArgs.modID == Versioner.MOD_ID) {
            ConfigManager.sync(Versioner.MOD_ID, Config.Type.INSTANCE)
        }
    }
}
