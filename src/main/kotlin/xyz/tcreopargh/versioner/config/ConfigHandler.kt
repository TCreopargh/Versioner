/**
 * @author TCreopargh
 */
@file:Config(modid = "versioner")
package xyz.tcreopargh.versioner.config

import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.Config.LangKey

@LangKey("versioner.config.enable_version_checking")
@Config.Comment(
    "If this is set to false, the mod will not try to fetch the version data at all. ",
    "It's a switch for modpack users that want to disable modpack version checking."
)
@JvmField
var enableVersionChecking = true

@LangKey("versioner.config.changelog_prefix")
@Config.Comment("Extra text added before each line of changelog")
@JvmField
var changelogPrefix = " - "

@LangKey("versioner.config.read_timeout")
@Config.Comment("How much time to read before the connection closes with a timeout error.")
@JvmField
var versionCheckerReadTimeout = 5000

@LangKey("versioner.config.connect_timeout")
@Config.Comment("How much time to connect to the URL before the connection closes with a timeout error.")
@JvmField
var versionCheckerConnectTimeout = 5000

@LangKey("versioner.config.modpack_name")
@Config.Comment("The name of your modpack")
@JvmField
var modpackName = ""

@LangKey("versioner.config.version_data_url")
@Config.Comment("Where the version data JSON will be fetched from, KEEP http:// or https://")
@JvmField
var versionDataURL = ""

@LangKey("versioner.config.update_url")
@Config.Comment("The url that is opened when the user clicks on update button, KEEP http:// or https://")
@JvmField
var updateURL = ""

@LangKey("versioner.config.delimiter")
@Config.Comment("The border of large messages, makes them look pretty")
@JvmField
var delimiter = "========================================"

@LangKey("versioner.config.notifications_category")
@Config.Comment("Update notifications & messages")
@JvmField
var versionNotifications = VersionNotifications()

class VersionNotifications {

    @LangKey("versioner.config.notifications.show_login_chat_message")
    @Config.Comment("Display a chat message when logging into a world for the first time if an update is available?")
    @JvmField
    var showLoginChatUpdateNotification = true

    @LangKey("versioner.config.notifications.show_welcome_message")
    @Config.Comment("If 'welcomeMessage' key is defined in the JSON, display the message in chat when logging into a world")
    @JvmField
    var showWelcomeMessage = true

    @LangKey("versioner.config.notifications.show_update_check_failed_message")
    @Config.Comment("If true, displays an error message in chat if this mod is unable to fetch all update data.")
    @JvmField
    var showUpdateCheckFailedMessage = true
}

@LangKey("versioner.config.current_version_category")
@Config.Comment(
    "Config for setting info of current version",
    "For modpack devs: make sure to change these when you update the modpack!"
)
@JvmField
var currentVersion = CurrentVersion()

class CurrentVersion {
    @LangKey("versioner.config.current_version.version_name")
    @Config.Comment("Version name of the current version")
    @JvmField
    var versionName = "1.0.0"

    @LangKey("versioner.config.current_version.version_code")
    @Config.Comment("Version code of the current version, must not be negative.")
    @JvmField
    var versionCode = 0

    @LangKey("versioner.config.current_version.version_format")
    @Config.Comment("How to output formatted version name. Only used for displaying current version.")
    @JvmField
    var versionFormat = "%versionName%"

    @LangKey("versioner.config.current_version.variables")
    @Config.Comment("Variables to use when formatting current version. One entry per line, the format is key=value")
    @JvmField
    var variables: Array<String> = arrayOf()
}

