package xyz.tcreopargh.versioner.util

/**
 * @author TCreopargh
 */
data class Coords(val x: Int, val y: Int) {

    operator fun plus(another: Coords) = Coords(x + another.x, y + another.y)

    fun add(x: Int = 0, y: Int = 0) = Coords(this.x + x, this.y + y)

    operator fun minus(another: Coords) = Coords(x - another.x, y - another.y)

    fun sub(x: Int = 0, y: Int = 0) = Coords(this.x - x, this.y - y)
}
