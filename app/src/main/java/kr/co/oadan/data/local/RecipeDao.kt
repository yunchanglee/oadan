package kr.co.oadan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.co.oadan.data.model.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE stage = :stage")
    fun getRecipesByStage(stage: String): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getRecipeById(id: String): Recipe?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<Recipe>)

    @Update
    suspend fun update(recipe: Recipe)

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun count(): Int
}
