package xyz.tcreopargh.versioner

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

object NetworkHandler {
    @Throws(IOException::class)
    fun readToString(targetURL: String?): String {
        val url = URL(targetURL)
        val bufferedReader = BufferedReader(
            InputStreamReader(url.openConnection().apply {
                this.connectTimeout = versionCheckerConnectTimeout
                this.readTimeout = versionCheckerReadTimeout
            }.getInputStream())
        )
        val stringBuilder = StringBuilder()
        while(true) {
            val line: String = bufferedReader.readLine() ?: break
            stringBuilder.append(line)
        }
        bufferedReader.close()
        return stringBuilder.toString().trim()
    }
}
