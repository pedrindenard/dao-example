package com.app.musicapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.musicapp.model.Music

@Entity(tableName = "music")
data class MusicEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "actor") val actor: String,
    @ColumnInfo(name = "runtime") val runtime: String
) {
    val music: Music
        get() = Music(
            id,
            name,
            actor,
            runtime,
            image
        )
}