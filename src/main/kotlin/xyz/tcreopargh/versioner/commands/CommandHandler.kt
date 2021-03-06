package xyz.tcreopargh.versioner.commands

import com.sun.org.apache.xml.internal.security.utils.I18n
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.client.IClientCommand
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import xyz.tcreopargh.versioner.Versioner.versionData

/**
 * @author TCreopargh
 */
object CommandHandler {

    const val SPONSORS_COMMAND_NAME = "sponsors"
    const val SPONSORS_COMMAND_ARG_LIST = "list"
    const val SPONSORS_COMMAND_ARG_CHECK = "check"

    @SideOnly(Side.CLIENT)
    class SponsorsCommand : CommandBase(), IClientCommand {
        override fun getName(): String {
            return SPONSORS_COMMAND_NAME
        }

        override fun getUsage(p0: ICommandSender): String {
            return I18n.translate("versioner.command.sponsors.usage")
        }

        override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
            when (args.getOrNull(0)) {
                SPONSORS_COMMAND_ARG_LIST -> {
                    val msg: MutableList<ITextComponent>? = versionData?.sponsors?.getFormattedText()
                    if (msg != null) {
                        for (line in msg) {
                            sender.sendMessage(line)
                        }
                    }
                }
                SPONSORS_COMMAND_ARG_CHECK -> {
                    val playerName = args.getOrNull(1)
                    val player: EntityPlayer? = server.playerList.getPlayerByUsername(playerName ?: "null")
                    if (player != null) {
                        val category = versionData?.sponsors?.checkPlayer(player)
                        if (category != null) {
                            sender.sendMessage(
                                TextComponentTranslation(
                                    "versioner.command.sponsors.check_true",
                                    player.name, category.getFormattedName().formattedText
                                )
                            )
                        } else {
                            sender.sendMessage(
                                TextComponentTranslation(
                                    "versioner.command.sponsors.check_false",
                                    player.name
                                )
                            )
                        }
                    }
                }
            }
        }

        override fun allowUsageWithoutPrefix(p0: ICommandSender?, p1: String?): Boolean {
            return false
        }
    }
}
