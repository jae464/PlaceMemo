package com.jae464.domain.repository

import com.jae464.domain.model.post.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insertCategory()
    fun getAllCategory(): Flow<List<Category>>
    suspend fun deleteCategory()

}