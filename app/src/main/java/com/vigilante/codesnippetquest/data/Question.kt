package com.vigilante.codesnippetquest.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "level") val level: Int,
    @ColumnInfo(name = "snippet") val snippet: String,
    @ColumnInfo(name = "question_text") val text: String,
    @ColumnInfo(name = "question_text_ar") val textAr: String,
    @ColumnInfo(name = "option_a") val opA: String,
    @ColumnInfo(name = "option_b") val opB: String,
    @ColumnInfo(name = "option_c") val opC: String,
    @ColumnInfo(name = "option_d") val opD: String,
    @ColumnInfo(name = "correct_answer") val correct: String,
    @ColumnInfo(name = "hint") val hint: String
)
