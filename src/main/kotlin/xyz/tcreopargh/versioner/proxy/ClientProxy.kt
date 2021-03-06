package xyz.tcreopargh.versioner.proxy

import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import xyz.tcreopargh.versioner.Versioner
import xyz.tcreopargh.versioner.commands.CommandHandler.SponsorsCommand

open class ClientProxy : CommonProxy() {
    override fun postInit(event: FMLPostInitializationEvent?) {
        Versioner.logger?.info("Registering Command: " + SponsorsCommand.NAME)
        ClientCommandHandler.instance.registerCommand(SponsorsCommand())
    }
}
