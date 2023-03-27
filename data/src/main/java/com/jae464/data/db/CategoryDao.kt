package com.jae464.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jae464.data.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Query("DELETE FROM category WHERE category_id = :categoryId")
    suspend fun deleteCategory(categoryId: Long)

    @Query("SELECT * FROM category")
    fun getAllCategory(): Flow<List<CategoryEntity>>

}