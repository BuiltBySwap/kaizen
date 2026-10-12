package dev.builtbyswap

class PlanRepository(csvText: String) {
    val days: List<PlanDay>
    private val byDate: Map<String, PlanDay>
    private val questions: Map<Int, Question>

    init {
        val rows = parseCsv(csvText)
        val header = rows.first()
        val index = header.withIndex().associate { (i, name) -> name to i }
        fun List<String>.col(name: String) = this[index.getValue(name)]

        days = rows.drop(1).filter { it.size == header.size }.map { r ->
            PlanDay(
                date = r.col("date"), id = r.col("id"), track = r.col("track"), priority = r.col("priority"),
                load = r.col("load"), hours = r.col("hours").toDouble(), topic = r.col("topic"), why = r.col("why"),
                steps = r.col("steps").lines().filter { it.isNotBlank() },
                tool = r.col("tool"), leetcode = r.col("leetcode"),
                mobileQuestion = r.col("mobile_q"), english = r.col("english"),
            )
        }
        byDate = days.associateBy { it.date }

        val pattern = Regex("""MQ(\d+) \[(.*?)] (.*)""")
        questions = days.flatMap { day ->
            day.mobileQuestion.lines().mapNotNull { line ->
                pattern.matchEntire(line.trim())?.let { m ->
                    Question(m.groupValues[1].toInt(), m.groupValues[2], m.groupValues[3], day.date)
                }
            }
        }.associateBy { it.number }
    }

    fun forDate(date: String): PlanDay? = byDate[date]
    fun question(number: Int): Question? = questions[number]

    companion object {
        fun fromResource(path: String): PlanRepository {
            val text = PlanRepository::class.java.getResourceAsStream(path)
                ?.bufferedReader()?.use { it.readText() }
                ?: error("Resource not found: $path")
            return PlanRepository(text)
        }
    }
}