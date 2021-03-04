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
            InputStreamReader(url.openStream())
        )
        val stringBuilder = StringBuilder()
        var inputLine: String
        while (bufferedReader.readLine().also {
                inputLine = it
            } != null) {
            stringBuilder.append(inputLine)
            stringBuilder.append(System.lineSeparator())
        }
        bufferedReader.close()
        return stringBuilder.toString().trim()
    }
}
