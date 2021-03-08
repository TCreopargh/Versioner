package xyz.tcreopargh.versioner.mainmenu

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import xyz.tcreopargh.versioner.Versioner
import xyz.tcreopargh.versioner.config.mainMenu
import xyz.tcreopargh.versioner.util.Coords
import xyz.tcreopargh.versioner.util.getMainMenuTexts

/**
 * @author TCreopargh
 */
@Mod.EventBusSubscriber(modid = Versioner.MOD_ID)
@SideOnly(Side.CLIENT)
object GuiHandler {
    @SubscribeEvent
    fun onRenderMainMenu(event: GuiScreenEvent) {
        if (event.gui !is GuiMainMenu) {
            return
        }
        val position: MenuPosition = MenuPosition.fromString(mainMenu.menuTextPosition)
        val marginVertical = mainMenu.marginVertical
        val marginHorizontal = mainMenu.marginHorizontal
        val scale = ScaledResolution(Minecraft.getMinecraft())
        val fontRenderer = Minecraft.getMinecraft().fontRenderer
        val color = mainMenu.textColor
        val lines = getMainMenuTexts()
        val stringHeight = 10
        var longestLineIndex = 0
        for (line in lines) {
            if (line.length > longestLineIndex) {
                longestLineIndex = line.length
            }
        }
        fun getStringWidth() = fontRenderer.getStringWidth(lines[longestLineIndex])
        fun getStringHeight() = stringHeight * lines.size
        val startingPosition: Coords = when (position) {
            MenuPosition.TOP_LEFT     -> Coords(marginHorizontal, marginVertical)
            MenuPosition.TOP_RIGHT    -> Coords(
                scale.scaledWidth - getStringWidth() - marginHorizontal,
                marginVertical
            )
            MenuPosition.BOTTOM_LEFT  -> Coords(
                marginHorizontal,
                scale.scaledHeight - marginVertical - stringHeight - getStringHeight()
            )
            MenuPosition.BOTTOM_RIGHT -> Coords(
                scale.scaledWidth - getStringWidth() - marginHorizontal,
                scale.scaledHeight - marginVertical - stringHeight - getStringHeight()
            )
        }
        var pos = startingPosition.copy()
        for (i in lines.indices) {
            val line = lines[i]
            fontRenderer.drawStringWithShadow(line, pos.x.toFloat(), pos.y.toFloat(), color)
            pos = pos.add(y = stringHeight)
        }
    }
}
