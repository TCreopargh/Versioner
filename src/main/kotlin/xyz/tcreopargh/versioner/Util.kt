package xyz.tcreopargh.versioner

import net.minecraft.client.resources.I18n
import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import javax.swing.JOptionPane

typealias ChangelogMap = MutableMap<String?, List<String?>>?

fun compareVersionNames(a: String, b: String): Int {
    val separator = Regex("[^\\d]+")
    val arrayA = a.split(separator)
    val arrayB = b.split(separator)
    var i = 0
    while (i < arrayA.size || i < arrayB.size) {
        val seqA = arrayA[i]
        val seqB = arrayB[i]
        if (seqA.toIntOrNull() != null && seqB.toIntOrNull() != null) {
            when {
                (seqA.toInt() > seqB.toInt()) ->
                    return 1
                (seqA.toInt() < seqB.toInt()) ->
                    return -1
            }
        }
        i++
    }
    return 0
}

fun showUpdateDialog() {
    val options = arrayOf(
        I18n.format("versioner.update_dialog_option.yes"),
        I18n.format("versioner.update_dialog_option.no")
    )
    val input = JOptionPane.showOptionDialog(
        null,
        I18n.format(
            "versioner.update_dialog_message",
            modpackName,
            Versioner.versionData?.versionName,
            currentVersion
        ),
        I18n.format("versioner.update_dialog_title"),
        JOptionPane.QUESTION_MESSAGE,
        JOptionPane.YES_NO_OPTION, null, options, options[1]
    )
    if (input == 0) {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            try {
                desktop.browse(URI(updateURL))
            } catch (ignored: IOException) {
            } catch (ignored: URISyntaxException) {
            }
        }
    }
}

