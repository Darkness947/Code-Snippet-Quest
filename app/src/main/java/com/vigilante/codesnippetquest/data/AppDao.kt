package com.vigilante.codesnippetquest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun loginUser(username: String, password: String): User?

    @Query("SELECT username FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUsernameById(userId: Int): String?

    @Query("SELECT unlocked_level FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUnlockedLevel(userId: Int): Int?

    @Query("SELECT unlocked_level FROM users WHERE id = :userId LIMIT 1")
    fun getUnlockedLevelFlow(userId: Int): Flow<Int?>

    @Query("UPDATE users SET unlocked_level = :level WHERE id = :userId")
    suspend fun updateUnlockedLevel(userId: Int, level: Int): Int

    @Query("SELECT * FROM questions WHERE level = :level")
    suspend fun getQuestionsForLevel(level: Int): List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistoryRecord(record: HistoryRecord): Long

    @Query("SELECT * FROM history WHERE user_id = :userId ORDER BY id DESC")
    fun getHistoryRecords(userId: Int): Flow<List<HistoryRecord>>

    @Query("DELETE FROM history WHERE user_id = :userId")
    suspend fun clearHistory(userId: Int): Int
}
