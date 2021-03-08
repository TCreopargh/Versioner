package xyz.tcreopargh.versioner.compat.crafttweaker

import crafttweaker.annotations.ZenRegister
import crafttweaker.api.data.IData
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod
import xyz.tcreopargh.versioner.config.currentVersion
import xyz.tcreopargh.versioner.util.CT_NAMESPACE
import xyz.tcreopargh.versioner.util.currentVariables

/**
 * Note: Don't try to add ZenGetters or ZenSetters, they are bugged when used with ZenMethods
 * @author TCreopargh
 */
@ZenClass(CT_NAMESPACE + "CurrentVersion")
@ZenRegister
class CurrentVersionCT {
    @ZenMethod
    fun getVersionName() = currentVersion.versionName

    @ZenMethod
    fun getVersionCode() = currentVersion.versionCode

    @ZenMethod
    fun getVersionFormat() = currentVersion.versionFormat

    @ZenMethod
    fun getVariable(name: String): IData? = currentVariables[name]
}
