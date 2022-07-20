package com.jae464.placememo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jae464.placememo.data.model.*

@Dao
interface MemoDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertMemo(memo: MemoEntity)

   @Query("SELECT * FROM memo WHERE id = :id")
   suspend fun getMemo(id: Long): MemoEntity

}