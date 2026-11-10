package kr.co.oadan.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.co.oadan.data.local.InitialRecipes
import kr.co.oadan.data.local.MealLogDao
import kr.co.oadan.data.local.RecipeDao
import kr.co.oadan.data.model.MealLog
import kr.co.oadan.data.model.Recipe
import kr.co.oadan.domain.OrganicEvaluation
import kr.co.oadan.domain.OrganicScoreCalculator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MealRepository(
    private val mealLogDao: MealLogDao,
    private val recipeDao: RecipeDao
) {

    fun getRecommendedStage(fastedHours: Float): String {
        return when {
            fastedHours < 14f -> "일반 보식"
            fastedHours < 18f -> "부드러운 보식"
            fastedHours < 24f -> "순한 보식"
            else -> "회복 보식"
        }
    }

    suspend fun ensureInitialRecipes() {
        if (recipeDao.count() == 0) {
            recipeDao.insertAll(InitialRecipes.list)
        }
    }

    fun getAllRecipes(): Flow<List<Recipe>> = recipeDao.getAllRecipes()

    fun getRecipesForHours(fastedHours: Float): Flow<List<Recipe>> {
        val stage = getRecommendedStage(fastedHours)
        return recipeDao.getAllRecipes().map { list ->
            // 우선 권장 단계 레시피를 상단에 배치
            list.sortedByDescending { it.stage == stage }
        }
    }

    suspend fun getRecipeById(id: String): Recipe? = recipeDao.getRecipeById(id)

    suspend fun toggleBookmark(recipeId: String) {
        val recipe = recipeDao.getRecipeById(recipeId) ?: return
        recipeDao.update(recipe.copy(isBookmarked = !recipe.isBookmarked))
    }

    suspend fun saveMealLog(
        menuName: String,
        fastSessionId: Long? = null,
        photoUri: String? = null,
        checkVegFirst: Boolean = false,
        checkNoProcessed: Boolean = false,
        checkSlow20m: Boolean = false,
        checkNoSweetDrink: Boolean = false,
        checkSeasonal: Boolean = false,
        notes: String? = null
    ): Pair<Long, OrganicEvaluation> {
        val eval = OrganicScoreCalculator.calculate(
            checkVegFirst = checkVegFirst,
            checkNoProcessed = checkNoProcessed,
            checkSlow20m = checkSlow20m,
            checkNoSweetDrink = checkNoSweetDrink,
            checkSeasonal = checkSeasonal
        )

        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val log = MealLog(
            fastSessionId = fastSessionId,
            date = dateStr,
            menuName = menuName,
            photoUri = photoUri,
            checkVegFirst = checkVegFirst,
            checkNoProcessed = checkNoProcessed,
            checkSlow20m = checkSlow20m,
            checkNoSweetDrink = checkNoSweetDrink,
            checkSeasonal = checkSeasonal,
            organicScore = eval.score,
            organicGrade = eval.grade.title,
            feedbackMessage = eval.feedbackMessage,
            notes = notes
        )
        val id = mealLogDao.insert(log)
        return Pair(id, eval)
    }

    fun getAllMealLogs(): Flow<List<MealLog>> = mealLogDao.getAllMealLogs()

    suspend fun getAllMealLogsOnce(): List<MealLog> = mealLogDao.getAllMealLogsOnce()

    suspend fun getMealLogById(id: Long): MealLog? = mealLogDao.getMealLogById(id)

    suspend fun getMealLogBySessionId(sessionId: Long): MealLog? = mealLogDao.getMealLogBySessionId(sessionId)
}
