package com.vigilante.codesnippetquest.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "history",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HistoryRecord(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "level_name") val levelName: String,
    @ColumnInfo(name = "level_number") val levelNumber: Int = 1,
    @ColumnInfo(name = "score_percentage") val score: Int,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "date") val date: String
)
