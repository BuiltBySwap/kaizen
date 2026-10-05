package dev.builtbyswap.kaizen.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.builtbyswap.kaizen.domain.model.PlanDay
import dev.builtbyswap.kaizen.domain.repository.PlanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads the bundled plan snapshot (assets/plan.json).
 * Temporary: PRJ-02 moves the data behind the Ktor API, and PRJ-04 adds Room as the local cache.
 */
@Singleton
class AssetPlanRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : PlanRepository {

    override suspend fun getAll(): List<PlanDay> = withContext(Dispatchers.IO) {
        val json = context.assets.open("plan.json").bufferedReader().use { it.readText() }
        val array = JSONArray(json)
        List(array.length()) { index -> array.getJSONObject(index).toPlanDay() }
    }
}

private fun JSONObject.toPlanDay(): PlanDay {
    val stepsArray = optJSONArray("steps")
    return PlanDay(
        date = LocalDate.parse(getString("date")),
        id = getString("id"),
        track = optString("track"),
        priority = optString("priority"),
        load = optString("load"),
        hours = optDouble("hours", 0.0),
        topic = getString("topic"),
        why = optString("why"),
        steps = stepsArray?.let { array -> List(array.length()) { array.getString(it) } } ?: emptyList(),
        tool = optString("tool"),
        leetCode = optString("leetcode"),
        mobileQuestion = optString("mobile_q"),
        english = optString("english"),
    )
}
