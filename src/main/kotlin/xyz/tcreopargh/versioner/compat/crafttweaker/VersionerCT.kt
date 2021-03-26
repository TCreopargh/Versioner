package xyz.tcreopargh.versioner.compat.crafttweaker

import crafttweaker.annotations.ZenRegister
import crafttweaker.api.data.IData
import crafttweaker.api.minecraft.CraftTweakerMC
import crafttweaker.api.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod
import xyz.tcreopargh.versioner.Versioner.versionData
import xyz.tcreopargh.versioner.config.modpackName
import xyz.tcreopargh.versioner.util.CT_NAMESPACE
import xyz.tcreopargh.versioner.util.getDataFromJsonElement
import xyz.tcreopargh.versioner.util.getMainMenuTexts
import xyz.tcreopargh.versioner.util.getUpdateChatMessage
import java.util.stream.Collectors

/**
 * Note: Don't try to add ZenGetters or ZenSetters, they are bugged when used with ZenMethods
 * @author TCreopargh
 */
@ZenClass(CT_NAMESPACE + "Versioner")
@ZenRegister
object VersionerCT {

    @ZenMethod
    @JvmStatic
    fun getCurrentVersion(): CurrentVersionCT = CurrentVersionCT()

    @ZenMethod
    @JvmStatic
    fun getVersionName(): String? = versionData?.versionName

    @ZenMethod
    @JvmStatic
    fun getVersionCode(): Int = versionData?.versionCode ?: -1

    @ZenMethod
    @JvmStatic
    fun getVersionFormat(): String? = versionData?.versionFormat

    @ZenMethod
    @JvmStatic
    fun getFormattedString(format: String?): String? = getFormattedString(format ?: "")

    @ZenMethod
    @JvmStatic
    fun getChangelogsMap(): Map<String, List<String>>? = versionData?.changelogs?.map

    @ZenMethod
    @JvmStatic
    fun getWelcomeMessage(): String? = versionData?.welcomeMessage

    @ZenMethod
    @JvmStatic
    fun getSponsorMessage(): String? = versionData?.sponsorMessage

    @ZenMethod
    @JvmStatic
    fun getUpdateLink(): String? = getUpdateLink()

    @ZenMethod
    @JvmStatic
    fun isUpdateAvailable(): Boolean = versionData?.isUpdateAvailable() ?: false

    @ZenMethod
    @JvmStatic
    fun getVersionData(): IData = getDataFromJsonElement(versionData?.jsonObj)

    @ZenMethod
    @JvmStatic
    fun getVariableData(): IData = getDataFromJsonElement(versionData?.variables)

    @ZenMethod
    @JvmStatic
    fun getUpdateMessage(): List<ITextComponent> = getUpdateChatMessage().stream().map { v ->
        CraftTweakerMC.getITextComponent(v)
    }.collect(Collectors.toList())

    @ZenMethod
    @JvmStatic
    fun getSponsors(): SponsorsCT? {
        val data = versionData?.sponsors
        if (data != null) {
            return SponsorsCT(data)
        }
        return null
    }

    @ZenMethod
    @JvmStatic
    fun isReady(): Boolean = versionData?.isReady ?: false

    @ZenMethod
    @JvmStatic
    fun getModpackName(): String = modpackName

    @ZenMethod
    @JvmStatic
    fun getTranslatedModpackName(): ITextComponent = CraftTweakerMC.getITextComponent(
        TextComponentTranslation
            (modpackName)
    )

    @ZenMethod
    @JvmStatic
    fun getMainMenuText(): List<String> = getMainMenuTexts()

    @ZenMethod
    @JvmStatic
    fun getMainMenuTextTooltip(): List<String> = getMainMenuTextTooltip()

    @ZenMethod
    @JvmStatic
    fun hasVariable(name: String): Boolean = versionData?.variables?.has(name) ?: false

    @ZenMethod
    @JvmStatic
    fun getVariable(name: String): IData = getDataFromJsonElement(versionData?.getVariable(name))
}
