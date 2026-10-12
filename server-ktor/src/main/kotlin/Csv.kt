package dev.builtbyswap

fun parseCsv(text: String): List<List<String>> {
    val rows = mutableListOf<List<String>>()
    var row = mutableListOf<String>()
    val field = StringBuilder()
    var inQuotes = false
    var i = 0
    while (i < text.length) {
        val c = text[i]
        when {
            inQuotes && c == '"' && i + 1 < text.length && text[i + 1] == '"' -> { field.append('"'); i++ }
            c == '"' -> inQuotes = !inQuotes
            !inQuotes && c == ',' -> { row.add(field.toString()); field.clear() }
            !inQuotes && (c == '\n' || c == '\r') -> {
                if (c == '\r' && i + 1 < text.length && text[i + 1] == '\n') i++
                row.add(field.toString()); field.clear()
                rows.add(row); row = mutableListOf()
            }
            else -> field.append(c)
        }
        i++
    }
    if (field.isNotEmpty() || row.isNotEmpty()) { row.add(field.toString()); rows.add(row) }
    return rows
}