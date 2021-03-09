package xyz.tcreopargh.versioner.compat.crafttweaker

import crafttweaker.annotations.ZenRegister
import crafttweaker.api.data.IData
import crafttweaker.api.minecraft.CraftTweakerMC
import crafttweaker.api.player.IPlayer
import crafttweaker.api.text.ITextComponent
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod
import xyz.tcreopargh.versioner.data.SponsorData
import xyz.tcreopargh.versioner.util.CT_NAMESPACE
import xyz.tcreopargh.versioner.util.getDataFromJsonElement
import java.util.stream.Collectors

/**
 * Note: Don't try to add ZenGetters or ZenSetters, they are bugged when used with ZenMethods
 * @author TCreopargh
 */
@ZenClass(CT_NAMESPACE + "Sponsors")
@ZenRegister
class SponsorsCT(val internal: SponsorData) {

    @ZenMethod
    fun isPlayerInCategory(player: IPlayer, category: String): Boolean {
        val mcPlayer = CraftTweakerMC.getPlayer(player)
        if (mcPlayer != null) {
            return internal.isPlayerInCategory(mcPlayer, category)
        }
        return false
    }

    @ZenMethod
    fun isSponsor(player: IPlayer): Boolean {
        val mcPlayer = CraftTweakerMC.getPlayer(player)
        if (mcPlayer != null) {
            return internal.isSponsor(mcPlayer)
        }
        return false
    }

    @ZenMethod
    fun getPlayerCategory(player: IPlayer): String? {
        val mcPlayer = CraftTweakerMC.getPlayer(player)
        if (mcPlayer != null) {
            return internal.checkPlayer(mcPlayer)?.toString() as String
        }
        return null
    }

    @ZenMethod
    fun getCategories(): List<String> = internal.categoryList.stream().map { v ->
        v.name
    }.collect(Collectors.toList())

    @ZenMethod
    fun getFormattedCategory(category: String): ITextComponent? {
        val text = internal[category]?.getFormattedName()
        return if (text != null) CraftTweakerMC.getITextComponent(text) else null
    }

    @ZenMethod
    fun getFormattedCategorySponsors(category: String): List<ITextComponent?>? {
        return internal[category]?.getFormattedText()?.stream()?.map { v ->
            if (v != null) CraftTweakerMC.getITextComponent(v) else null
        }?.collect(Collectors.toList())
    }

    @ZenMethod
    fun getAsData(): IData = getDataFromJsonElement(internal.jsonArray)
}
