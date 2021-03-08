package xyz.tcreopargh.versioner.mainmenu

import org.apache.logging.log4j.Level
import xyz.tcreopargh.versioner.Versioner

enum class MenuPosition {

    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT;

    companion object {
        fun fromString(value: String): MenuPosition {
            var ret = TOP_LEFT
            try {
                ret = valueOf(value)
            } catch (e: IllegalArgumentException) {
                Versioner.logger?.log(
                    Level.ERROR, "$value is not a valid menu position! " +
                        "Must be one of: ${getValuesString()}"
                )
            }
            return ret
        }

        fun getValuesString(): String {
            val sb = StringBuilder()
            for (value in values()) {
                sb.append(value.name).append(", ")
            }
            if (sb.length >= 2) {
                sb.removeRange(sb.length - 2 until sb.length)
            }
            return sb.toString()
        }
    }
}
