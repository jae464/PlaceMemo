package com.jae464.placememo.data.db

import androidx.room.*
import com.jae464.placememo.data.model.*
import com.jae464.placememo.domain.model.post.Memo

@Dao
interface MemoDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertMemo(memo: MemoEntity): Long

   @Update
   suspend fun updateMemo(memo: MemoEntity)

   @Query("DELETE FROM memo WHERE id = :id")
   fun deleteMemo(id: Long)

   @Query("SELECT * FROM memo WHERE id = :id")
   suspend fun getMemo(id: Long): MemoEntity

   @Query("SELECT * FROM memo")
   suspend fun getAllMemo(): List<MemoEntity>

   @Query("SELECT * FROM memo WHERE category = :category")
   suspend fun getMemoByCategory(category: Int): List<MemoEntity>

   @Query("SELECT * FROM memo WHERE title LIKE '%' || :title || '%'")
   suspend fun getMemoByTitle(title: String): List<MemoEntity>

}