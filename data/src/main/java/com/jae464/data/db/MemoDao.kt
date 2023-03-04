package com.jae464.data.db

import androidx.room.*
import com.jae464.data.model.FolderEntity
import com.jae464.data.model.FolderWithMemos
import com.jae464.data.model.MemoEntity

@Dao
interface MemoDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertMemo(memo: MemoEntity): Long

   @Update
   suspend fun updateMemo(memo: MemoEntity)

   @Query("DELETE FROM memo WHERE memo_id = :id")
   fun deleteMemo(id: Long)

   @Query("SELECT * FROM memo WHERE memo_id = :id")
   suspend fun getMemo(id: Long): MemoEntity

   @Query("SELECT * FROM memo")
   suspend fun getAllMemo(): List<MemoEntity>

   @Query("SELECT * FROM memo WHERE category = :category")
   suspend fun getMemoByCategory(category: Int): List<MemoEntity>

   @Query("SELECT * FROM memo WHERE title LIKE '%' || :title || '%'")
   suspend fun getMemoByTitle(title: String): List<MemoEntity>

   @Transaction
   @Query("SELECT * FROM folder")
   suspend fun getFoldersWithMemos(): List<FolderWithMemos>

}