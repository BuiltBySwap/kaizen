package dev.builtbyswap

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class ProgressStore {
    private val byDate = ConcurrentHashMap<String, Progress>()    // safe to share between request threads

    /** The new Progress, or null if that date already has one. */
    fun create(request: ProgressRequest): Progress? {
        val progress = Progress(request.date, request.status, request.note, Instant.now().toString())
        return if (byDate.putIfAbsent(request.date, progress) == null) progress else null
    }

    fun find(date: String): Progress? = byDate[date]
}