package xyz.tcreopargh.versioner.mainmenu

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.client.gui.GuiScreen
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
object GuiHandler {

    var mousePosStored = Coords(0, 0)
    var lastClick: Long = 0

    @SideOnly(Side.CLIENT)
    fun isMainMenu(gui: GuiScreen) =
        gui is GuiMainMenu || gui::class.qualifiedName == "lumien.custommainmenu.gui.GuiCustom"

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onRenderMainMenu(event: GuiScreenEvent.DrawScreenEvent) {
        if (!mainMenu.enableMainMenu) {
            return
        }
        if (!isMainMenu(event.gui)) {
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
    @SideOnly(Side.CLIENT)
    fun onMainMenuClick(event: GuiScreenEvent.MouseInputEvent) {
        if (!mainMenu.enableMainMenu) {
            return
        }
        if (!isMainMenu(event.gui) || !Mouse.isButtonDown(0)) {
            return
        }
        if (System.currentTimeMillis() - lastClick < 500) {
            return
        }
        lastClick = System.currentTimeMillis()
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
