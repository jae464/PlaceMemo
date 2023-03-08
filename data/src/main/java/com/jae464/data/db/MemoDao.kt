package com.jae464.data.db

import androidx.paging.PagingSource
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
   fun getAllMemo(): PagingSource<Long, MemoEntity>

   @Query("SELECT * FROM memo WHERE category = :category")
   fun getMemoByCategory(category: Int): PagingSource<Long, MemoEntity>

   @Query("SELECT * FROM memo WHERE title LIKE '%' || :title || '%'")
   fun getMemoByTitle(title: String): PagingSource<Long, MemoEntity>

   @Transaction
   @Query("SELECT * FROM folder")
   suspend fun getFoldersWithMemos(): List<FolderWithMemos>

}