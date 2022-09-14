package com.app.musicapp.dao

import androidx.room.*
import com.app.musicapp.entity.MusicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Query(value = "SELECT * FROM music")
    fun getAll(): Flow<List<MusicEntity>?>

    @Update
    suspend fun update(vararg entity: MusicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: MusicEntity)

    @Query(value = "DELETE FROM music WHERE id = :id")
    suspend fun delete(id: Int)
}