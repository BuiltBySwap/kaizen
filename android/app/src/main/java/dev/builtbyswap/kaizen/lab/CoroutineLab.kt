package dev.builtbyswap.kaizen.lab

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

suspend fun threePoints(): String {
    val tag = "id-" + System.nanoTime()
    var count = 1
    delay(100)
    count++
    delay(100)
    count++
    delay(100)
    return "$tag $count"
}