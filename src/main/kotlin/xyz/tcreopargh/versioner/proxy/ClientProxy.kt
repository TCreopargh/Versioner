package xyz.tcreopargh.versioner.proxy

import net.minecraft.client.resources.I18n

open class ClientProxy : CommonProxy() {
    override fun i18nSafe(key: String, vararg objects: Any): String {
        if (!I18n.hasKey(key)) {
            return key
        }
        return I18n.format(key, *objects)
    }
}
