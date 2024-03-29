package com.jae464.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.jae464.data.model.FolderEntity
import com.jae464.data.model.FolderWithMemos
import com.jae464.data.model.MemoEntity
import com.jae464.domain.model.post.Memo
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertMemo(memo: MemoEntity): Long

   @Update
   suspend fun updateMemo(memo: MemoEntity)

   @Query("DELETE FROM memo WHERE memo_id = :id")
   fun deleteMemo(id: Int)

   @Query("SELECT * FROM memo WHERE memo_id = :id")
   fun getMemo(id: Int): Flow<MemoEntity>

   @Query("SELECT * FROM memo")
   fun getAllMemo(): Flow<List<MemoEntity>>

   @Query("SELECT * FROM memo ORDER BY created_at DESC")
   fun getAllMemoWithPageSortByDesc(): PagingSource<Int, MemoEntity>

   @Query("SELECT * FROM memo ORDER BY created_at ASC")
   fun getAllMemoWithPageSortByAsc(): PagingSource<Int, MemoEntity>

   @Query("SELECT * FROM memo WHERE category_id = :categoryId")
   fun getMemoByCategory(categoryId: Long): Flow<List<MemoEntity>>

   @Query("SELECT * FROM memo WHERE category_id = :categoryId ORDER BY created_at DESC")
   fun getMemoByCategoryWithPageSortByDesc(categoryId: Long): PagingSource<Int, MemoEntity>

   @Query("SELECT * FROM memo WHERE category_id = :categoryId ORDER BY created_at ASC")
   fun getMemoByCategoryWithPageSortByAsc(categoryId: Long): PagingSource<Int, MemoEntity>

   @Query("SELECT * FROM memo WHERE folder_id = :folderId ORDER BY created_at DESC")
   fun getAllMemoByFolderSortByDescWithPage(folderId: Long): PagingSource<Int, MemoEntity>

   @Query("SELECT * FROM memo WHERE folder_id = :folderId ORDER BY created_at ASC")
   fun getAllMemoByFolderSortByAscWithPage(folderId: Long): PagingSource<Int, MemoEntity>

   @Query("SELECT * FROM memo WHERE folder_id = :folderId AND category_id = :categoryId ORDER BY created_at DESC")
   fun getMemoByFolderWithCategorySortByDescWithPage(folderId: Long, categoryId: Long): PagingSource<Int, MemoEntity>

   @Query("SELECT * FROM memo WHERE folder_id = :folderId AND category_id = :categoryId ORDER BY created_at ASC")
   fun getMemoByFolderWithCategorySortByAscWithPage(folderId: Long, categoryId: Long): PagingSource<Int, MemoEntity>

   @Query("SELECT * FROM memo WHERE title LIKE '%' || :title || '%'")
   fun getMemoByTitle(title: String): Flow<List<MemoEntity>>

   @Transaction
   @Query("SELECT * FROM folder WHERE folder_id = :folderId")
   suspend fun getFoldersWithMemos(folderId: Long): List<FolderWithMemos>

}