package com.jae464.data.repository.memo.local

import android.util.Log
import com.jae464.data.db.MemoDao
import com.jae464.data.manager.ImageManager
import com.jae464.data.model.FolderEntity
import com.jae464.data.model.FolderWithMemos
import com.jae464.data.model.MemoEntity
import javax.inject.Inject

class MemoLocalDataSourceImpl @Inject constructor(
    private val memoDao: MemoDao,
    private val imageManager: ImageManager
): MemoLocalDataSource {
    private val TAG = "MemoLocalDataSourceImpl"
    override suspend fun getMemo(id: Long): MemoEntity {
        return memoDao.getMemo(id)
    }

    override suspend fun getAllMemo(): List<MemoEntity> {
        // TODO START TEST CODE (create folder, get folder with memos)
        val folderId = memoDao.insertFolder(FolderEntity(0,"전체"))
        Log.d(TAG, "폴더 생성 완료 : id = $folderId")
        val folderWithMemos = memoDao.getFoldersWithMemos()
        Log.d(TAG, folderWithMemos.toString())
        // TODO END TEST CODE (create folder, get folder with memos)
        return memoDao.getAllMemo()
    }

    override suspend fun saveMemo(memo: MemoEntity): Long {
        return memoDao.insertMemo(memo)
    }

    override suspend fun updateMemo(memo: MemoEntity) {
        memoDao.updateMemo(memo)
    }

    override suspend fun getMemoByCategory(category: Int): List<MemoEntity> {
        return memoDao.getMemoByCategory(category)
    }

    override suspend fun getMemoByTitle(title: String): List<MemoEntity> {
        return memoDao.getMemoByTitle(title)
    }

    override suspend fun getMemoByContent(content: String): List<MemoEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMemo(id: Long) {
        memoDao.deleteMemo(id)
    }

    override suspend fun saveMemoImages(memoId: Long, imagePathList: List<String>) {
        imagePathList.forEach {imagePath ->
            imageManager.saveImage(memoId, imagePath)
        }
    }

    override suspend fun getFoldersWithMemos(): List<FolderWithMemos> {
        return memoDao.getFoldersWithMemos()
    }

    override fun getImagePathList(memoId: Long): List<String> {
        return imageManager.getImagePathList(memoId)

    }

}