package xyz.tcreopargh.versioner.mainmenu

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import xyz.tcreopargh.versioner.mainmenu.MenuPositionEnum.*
import xyz.tcreopargh.versioner.util.Coords
import xyz.tcreopargh.versioner.util.getClickLink
import java.awt.Desktop
import java.net.URI


@SideOnly(Side.CLIENT)
open class GuiLabel(
    val parent: GuiScreen,
    val mc: Minecraft = Minecraft.getMinecraft(),
    var texts: List<String> = listOf(),
    var tooltips: List<String> = listOf(),
    var color: Int = 0xffffff,
    var marginVertical: Int = 2,
    var marginHorizontal: Int = 2,
    var pos: MenuPositionEnum = TOP_LEFT,
    var scale: ScaledResolution = ScaledResolution(mc),
    val fontRenderer: FontRenderer = mc.fontRenderer.apply {
        unicodeFlag = true
    }
) : Gui() {

    companion object {
        const val STRING_HEIGHT = 10
    }

    open fun getLongestLineIndex(): Int {
        var longestLineIndex = 0
        for (i in texts.indices) {
            val line = texts[i]
            if (line.length > longestLineIndex) {
                longestLineIndex = i
            }
        }
        return longestLineIndex
    }

    open fun getStringWidth(): Int {
        val i = getLongestLineIndex()
        return if (i < texts.size) {
            fontRenderer.getStringWidth(texts[getLongestLineIndex()])
        } else {
            0
        }
    }

    open fun getStringHeight() = STRING_HEIGHT * texts.size

    open fun getStartingPosition() = when (pos) {
        TOP_LEFT      -> Coords(marginHorizontal, marginVertical)
        TOP_RIGHT     -> Coords(
            scale.scaledWidth - getStringWidth() - marginHorizontal,
            marginVertical
        )
        BOTTOM_LEFT   -> Coords(
            marginHorizontal,
            scale.scaledHeight - marginVertical - STRING_HEIGHT - getStringHeight()
        )
        BOTTOM_RIGHT  -> Coords(
            scale.scaledWidth - getStringWidth() - marginHorizontal,
            scale.scaledHeight - marginVertical - STRING_HEIGHT - getStringHeight()
        )
        CENTER        -> Coords(
            scale.scaledWidth / 2 - getStringWidth() / 2,
            scale.scaledWidth / 2 - STRING_HEIGHT / 2
        )
        TOP_CENTER    -> Coords(
            scale.scaledWidth / 2 - getStringWidth() / 2,
            marginVertical
        )
        BOTTOM_CENTER -> Coords(
            scale.scaledWidth / 2 - getStringWidth() / 2,
            scale.scaledHeight - marginVertical - STRING_HEIGHT - getStringHeight()
        )
        CENTER_LEFT   -> Coords(
            marginHorizontal,
            scale.scaledWidth / 2 - STRING_HEIGHT / 2
        )
        CENTER_RIGHT  -> Coords(
            scale.scaledWidth - getStringWidth() - marginHorizontal,
            scale.scaledWidth / 2 - STRING_HEIGHT / 2
        )
    }

    open fun draw() {
        var position = getStartingPosition().copy()
        for (i in texts.indices) {
            val line = texts[i]
            fontRenderer.drawStringWithShadow(line, position.x.toFloat(), position.y.toFloat(), color)
            position = position.add(y = STRING_HEIGHT)
        }
    }

    open fun hovered(mousePos: Coords) =
        mousePos.x in getStartingPosition().x until (getStartingPosition().x + getStringWidth()) &&
            mousePos.y in getStartingPosition().y until (getStartingPosition().y + (STRING_HEIGHT * texts.size))

    open fun mouseClicked(mousePos: Coords, mouseButton: Int) {
        if (hovered(mousePos)) {
            if (getClickLink() == "null") {
                return
            }
            mc.displayGuiScreen(
                GuiConfirmOpenLink(
                    { response, id ->
                        if (response && id == 0) {
                            try {
                                Desktop.getDesktop().browse(URI(getClickLink()))
                            } catch (ignored: Exception) {
                            }
                        }
                        mc.displayGuiScreen(parent)
                    },
                    getClickLink(), 0, true
                )
            )
        }
    }

    open fun drawHoveringText(coords: Coords) {
        val x = coords.x
        val y = coords.y
        val textLines = tooltips
        if (textLines.isNotEmpty()) {
            val width = mc.currentScreen?.width ?: 0
            val height = mc.currentScreen?.height ?: 0
            GlStateManager.disableDepth()
            var k = 0
            val iterator = textLines.iterator()
            while (iterator.hasNext()) {
                val s = iterator.next()
                val l = fontRenderer.getStringWidth(s)
                if (l > k) {
                    k = l
                }
            }
            var j2 = x + 12
            var k2 = y - 12
            var i1 = 8
            if (textLines.size > 1) {
                i1 += 2 + (textLines.size - 1) * 10
            }
            if (j2 + k > width) {
                j2 -= 28 + k
            }
            if (k2 + i1 + 6 > height) {
                k2 = height - i1 - 6
            }
            zLevel = 300.0f
            val j1 = -267386864
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1)
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1)
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1)
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1)
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1)
            val k1 = 1347420415
            val l1 = k1 and 16711422 shr 1 or k1 and -16777216
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1)
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1)
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1)
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1)
            for (i2 in textLines.indices) {
                val s1 = textLines[i2]
                fontRenderer.drawStringWithShadow(s1, j2.toFloat(), k2.toFloat(), -1)
                if (i2 == 0) {
                    k2 += 2
                }
                k2 += 10
            }
            zLevel = 0.0f
            GlStateManager.enableDepth()
        }
    }
}
