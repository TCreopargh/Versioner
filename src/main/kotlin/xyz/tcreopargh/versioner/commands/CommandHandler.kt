package xyz.tcreopargh.versioner.commands

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.*
import xyz.tcreopargh.versioner.Versioner.versionData
import xyz.tcreopargh.versioner.util.getAllPlayerNames

/**
 * @author TCreopargh
 */
object CommandHandler {

    class SponsorsCommand : CommandBase() {
        companion object {
            const val NAME = "sponsors"
            const val ARG_LIST = "list"
            const val ARG_CHECK = "check"
        }

        override fun getName(): String {
            return NAME
        }

        override fun getUsage(p0: ICommandSender): String {
            return "versioner.command.sponsors.usage"
        }

        override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
            when (args.getOrNull(0)) {
                ARG_LIST  -> {
                    val msg: MutableList<ITextComponent>? = versionData?.sponsors?.getFormattedText()
                    if (msg != null) {
                        for (line in msg) {
                            sender.sendMessage(line)
                        }
                    }
                }
                ARG_CHECK -> {
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
                else      -> {
                    sender.sendMessage(TextComponentString(getUsage(sender)).setStyle(Style().apply {
                        color = TextFormatting.RED
                    }))
                }
            }
        }

        override fun getTabCompletions(
            server: MinecraftServer,
            sender: ICommandSender,
            args: Array<out String>,
            pos: BlockPos?
        ): MutableList<String> {
            if (args.size == 1) {
                return getListOfStringsMatchingLastWord(args, ARG_LIST, ARG_CHECK)
            } else if (args[0] == ARG_CHECK && args.size == 2) {
                return getListOfStringsMatchingLastWord(args, server.playerList.getAllPlayerNames())
            }
            return mutableListOf()
        }

        override fun getRequiredPermissionLevel(): Int {
            return 0
        }
    }
}
