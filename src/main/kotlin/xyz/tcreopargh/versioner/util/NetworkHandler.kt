package xyz.tcreopargh.versioner.util

import xyz.tcreopargh.versioner.config.versionCheckerConnectTimeout
import xyz.tcreopargh.versioner.config.versionCheckerReadTimeout
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

/**
 * @author TCreopargh
 */
object NetworkHandler {
    @Throws(IOException::class)
    fun readToString(targetURL: String?): String {
        val url = URL(targetURL)
        val bufferedReader = BufferedReader(
            InputStreamReader(url.openConnection().apply {
                this.connectTimeout = versionCheckerConnectTimeout
                this.readTimeout = versionCheckerReadTimeout
            }.getInputStream(), Charsets.UTF_8)
        )
        val stringBuilder = StringBuilder()
        while (true) {
            val line: String = bufferedReader.readLine() ?: break
            stringBuilder.append(line)
        }
        bufferedReader.close()
        return stringBuilder.toString().trim()
    }
}
