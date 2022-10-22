package com.jae464.placememo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jae464.placememo.data.model.*
import com.jae464.placememo.domain.model.post.Memo

@Dao
interface MemoDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertMemo(memo: MemoEntity): Long

   @Query("SELECT * FROM memo WHERE id = :id")
   suspend fun getMemo(id: Long): MemoEntity

   @Query("SELECT * FROM memo")
   suspend fun getAllMemo(): List<MemoEntity>

   @Query("SELECT * FROM memo WHERE category = :category")
   suspend fun getMemoByCategory(category: Int): List<MemoEntity>

}