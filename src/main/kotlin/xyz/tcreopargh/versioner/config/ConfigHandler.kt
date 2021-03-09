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
@Config.Comment(
    "The url that is opened when the user clicks on update button, KEEP http:// or https://",
    "Note: This can be overridden by '"
)
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
    var versionFormat = "%currentVersionName%"

    @LangKey("versioner.config.current_version.variables")
    @Config.Comment("Variables to use when formatting current version. One entry per line, the format is key=value")
    @JvmField
    var variables: Array<String> = arrayOf()
}

@LangKey("versioner.config.mainmenu_category")
@Config.Comment(
    "Config for main menu renders"
)
@JvmField
var mainMenu = MainMenu()

class MainMenu {

    @LangKey("versioner.config.mainmenu.enable")
    @Config.Comment(
        "Whether to enable main menu rendering added by this mod at all or not. If set to false, all options below " +
            "will not work."
    )
    @JvmField
    var enableMainMenu = true

    @LangKey("versioner.config.mainmenu.text_position")
    @Config.Comment(
        "Where to place the main menu text. Must be one of these: 'TOP_LEFT', 'TOP_RIGHT', 'BOTTOM_LEFT', " +
            "'BOTTOM_RIGHT', 'TOP_CENTER', 'BOTTOM_CENTER', 'CENTER_LEFT', 'CENTER_RIGHT', 'CENTER'"
    )
    @JvmField
    var menuTextPosition = "TOP_LEFT"

    @LangKey("versioner.config.mainmenu.margin_horizontal")
    @Config.Comment(
        "How much space between main menu text and the border of the screen. (Horizontal) (Ignored when text position" +
            " is CENTER)"
    )
    @JvmField
    var marginHorizontal = 2


    @LangKey("versioner.config.mainmenu.margin_vertical")
    @Config.Comment(
        "How much space between main menu text and the border of the screen. (Vertical) (Ignored when text position" +
            " is CENTER)"
    )
    @JvmField
    var marginVertical = 2

    @LangKey("versioner.config.mainmenu.text_color")
    @Config.Comment(
        "Default color of the main menu text, although you can always use color codes to override this.",
        "Must be converted into a decimal integer. (0xffffff -> 16777215)"
    )
    @JvmField
    var textColor = 0xffffff

    @LangKey("versioner.config.mainmenu.text_lines")
    @Config.Comment(
        "Text to display on the main menu. You can use variables like %versionName% in the string.",
        "Note: This can get overridden by the fetched version data JSON!"
    )

    @JvmField
    var textLines: Array<String> = arrayOf(
        "§eVersion§f: §9%currentVersionName%",
        "%isUpdateAvailable%"
    )

    @LangKey("versioner.config.mainmenu.tooltip_text")
    @Config.Comment(
        "Text to display when hovering mouse over the text, as tooltips.",
        "Note: This can get overridden by the fetched version data JSON!"
    )
    @JvmField
    var tooltipText: Array<String> = arrayOf(
        "§eCurrent Version§f: §6%currentVersionName% (%currentVersionCode%)",
        "§2Latest Version§f: §2%versionName% (%versionCode%)",
        "§9§nClick for update link!"
    )

    @LangKey("versioner.config.mainmenu.click_link")
    @Config.Comment(
        "When the user click on the text, they will be sent to this link.",
        "Leave it empty to let it be the same as updateURL, or 'null' to not send them a link at all"
    )
    @JvmField
    var clickLink = ""
}
