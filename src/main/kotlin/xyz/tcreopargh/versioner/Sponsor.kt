package xyz.tcreopargh.versioner

import net.minecraft.entity.player.EntityPlayer
import java.util.*

data class Sponsor(val name: String? = null, val uuid: UUID? = null) {
    fun matchesPlayer(player: EntityPlayer): Boolean {
        if (uuid != null && player.uniqueID.equals(uuid)) {
            return true
        }
        if (uuid == null && name != null && player.name == name) {
            return true
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (other is Sponsor) {
            return this.name == other.name && this.uuid == other.uuid
        }
        return false
    }

    override fun hashCode(): Int {
        return name.hashCode() + uuid.hashCode()
    }

    override fun toString(): String {
        return name ?: uuid?.toString() ?: "null"
    }
}
