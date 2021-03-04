package xyz.tcreopargh.versioner

object Util {
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
}
