package com.jae464.data.repository.category

import com.jae464.data.db.CategoryDao
import com.jae464.data.model.CategoryEntity
import com.jae464.domain.model.post.Category
import com.jae464.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(
            category = CategoryEntity(
                id = category.id,
                name = category.name
            )
        )
    }

    override fun getAllCategory(): Flow<List<Category>> {
        return categoryDao.getAllCategory().map { list ->
            list.map { categoryEntity ->
                Category(
                    categoryEntity.id,
                    categoryEntity.name
                )
            }
        }
    }

    override suspend fun deleteCategory(categoryId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getCategoryById(categoryId: Long): Category {
        val categoryEntity = categoryDao.getCategoryById(categoryId)

        return Category(
            categoryEntity.id,
            categoryEntity.name
        )
    }

    override suspend fun isExistCategoryName(categoryName: String): Boolean {
        return categoryDao.isExistCategoryName(categoryName)
    }
}