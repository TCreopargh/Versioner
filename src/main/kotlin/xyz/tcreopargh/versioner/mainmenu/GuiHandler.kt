package xyz.tcreopargh.versioner.mainmenu

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMainMenu
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Mouse
import xyz.tcreopargh.versioner.Versioner
import xyz.tcreopargh.versioner.config.mainMenu
import xyz.tcreopargh.versioner.util.Coords
import xyz.tcreopargh.versioner.util.getMainMenuTexts
import xyz.tcreopargh.versioner.util.getMainMenuTooltipTexts

/**
 * @author TCreopargh
 */
@Mod.EventBusSubscriber(modid = Versioner.MOD_ID)
@SideOnly(Side.CLIENT)
object GuiHandler {

    var mousePosStored = Coords(0, 0)
    var lastClick: Long = 0

    @SubscribeEvent
    fun onRenderMainMenu(event: GuiScreenEvent.DrawScreenEvent) {
        if (event.gui !is GuiMainMenu) {
            return
        }
        val mc = Minecraft.getMinecraft()
        val position: MenuPositionEnum = MenuPositionEnum.fromString(mainMenu.menuTextPosition)
        val marginVertical = mainMenu.marginVertical
        val marginHorizontal = mainMenu.marginHorizontal
        val color = mainMenu.textColor
        val lines = getMainMenuTexts()
        val mousePos = Coords(event.mouseX, event.mouseY)
        val guiLabel = GuiLabel(
            parent = event.gui,
            mc = mc,
            texts = lines,
            tooltips = getMainMenuTooltipTexts(),
            color = color,
            marginVertical = marginVertical,
            marginHorizontal = marginHorizontal,
            pos = position
        )
        guiLabel.draw()
        if (guiLabel.hovered(mousePos)) {
            guiLabel.drawHoveringText(mousePos)
        }
        mousePosStored = mousePos.copy()
    }

    @SubscribeEvent
    fun onMainMenuClick(event: GuiScreenEvent.MouseInputEvent) {
        if (event.gui !is GuiMainMenu || !Mouse.isButtonDown(0)) {
            return
        }
        if (System.currentTimeMillis() - lastClick < 500) {
            return
        }
        lastClick = System.currentTimeMillis()
        val menu = event.gui as GuiMainMenu
        menu.handleMouseInput()
        val mc = Minecraft.getMinecraft()
        val position: MenuPositionEnum = MenuPositionEnum.fromString(mainMenu.menuTextPosition)
        val marginVertical = mainMenu.marginVertical
        val marginHorizontal = mainMenu.marginHorizontal
        val color = mainMenu.textColor
        val lines = getMainMenuTexts()
        val guiLabel = GuiLabel(
            parent = event.gui,
            mc = mc,
            texts = lines,
            tooltips = getMainMenuTooltipTexts(),
            color = color,
            marginVertical = marginVertical,
            marginHorizontal = marginHorizontal,
            pos = position
        )
        guiLabel.mouseClicked(
            mousePosStored, 0
        )
    }
}
