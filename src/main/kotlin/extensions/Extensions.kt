package kg.automoika.extensions

import kotlin.random.Random

fun generateId() : String {
    return Random.nextInt(0, 90000).toString()
}