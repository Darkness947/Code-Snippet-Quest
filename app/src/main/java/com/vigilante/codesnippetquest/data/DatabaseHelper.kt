package com.vigilante.codesnippetquest.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CodeSnippetQuest.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_UNLOCKED_LEVEL = "unlocked_level"

        const val TABLE_QUESTIONS = "questions"
        const val COLUMN_QUESTION_ID = "id"
        const val COLUMN_LEVEL = "level"
        const val COLUMN_SNIPPET = "snippet"
        const val COLUMN_QUESTION_TEXT = "question_text"
        const val COLUMN_OPTION_A = "option_a"
        const val COLUMN_OPTION_B = "option_b"
        const val COLUMN_OPTION_C = "option_c"
        const val COLUMN_OPTION_D = "option_d"
        const val COLUMN_CORRECT_ANSWER = "correct_answer"
        const val COLUMN_HINT = "hint"

        const val TABLE_HISTORY = "history"
        const val COLUMN_HISTORY_ID = "id"
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_LEVEL_NAME = "level_name"
        const val COLUMN_LEVEL_NUMBER = "level_number"
        const val COLUMN_SCORE_PERCENTAGE = "score_percentage"
        const val COLUMN_STATUS = "status"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_UNLOCKED_LEVEL INTEGER DEFAULT 1)")
        db?.execSQL(createUsersTable)

        val createQuestionsTable = ("CREATE TABLE $TABLE_QUESTIONS (" +
                "$COLUMN_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_LEVEL INTEGER, " +
                "$COLUMN_SNIPPET TEXT, " +
                "$COLUMN_QUESTION_TEXT TEXT, " +
                "$COLUMN_OPTION_A TEXT, " +
                "$COLUMN_OPTION_B TEXT, " +
                "$COLUMN_OPTION_C TEXT, " +
                "$COLUMN_OPTION_D TEXT, " +
                "$COLUMN_CORRECT_ANSWER TEXT, " +
                "$COLUMN_HINT TEXT)")
        db?.execSQL(createQuestionsTable)

        val createHistoryTable = ("CREATE TABLE $TABLE_HISTORY (" +
                "$COLUMN_HISTORY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USER_ID_FK INTEGER, " +
                "$COLUMN_LEVEL_NAME TEXT, " +
                "$COLUMN_LEVEL_NUMBER INTEGER DEFAULT 1, " +
                "$COLUMN_SCORE_PERCENTAGE INTEGER, " +
                "$COLUMN_STATUS TEXT, " +
                "$COLUMN_DATE TEXT, " +
                "FOREIGN KEY($COLUMN_USER_ID_FK) REFERENCES $TABLE_USERS($COLUMN_USER_ID))")
        db?.execSQL(createHistoryTable)

        insertInitialQuestions(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    private fun insertInitialQuestions(db: SQLiteDatabase?) {
        // Format: level, snippet, question, opA, opB, opC, opD, correct, hint
        val questions = listOf(
            arrayOf("1",
                "System.out.println(10 + 20 + \"Java\" + 10 + 20);",
                "What is the output of the code snippet above?",
                "30Java30", "1020Java1020", "30Java1020", "Compilation Error", "C",
                "Java evaluates expressions left to right. Before a String is encountered, integers are added. After a String, + becomes concatenation."),
            arrayOf("1",
                "String s1 = \"Code\";\nString s2 = new String(\"Code\");\nSystem.out.println(s1 == s2);",
                "What is the output?",
                "true", "false", "Compilation Error", "Runtime Exception", "B",
                "The == operator compares object references, not content. String literals use the string pool, but 'new String()' always creates a new object on the heap."),
            arrayOf("1",
                "int x = 1;\nswitch(x) {\n    case 1: System.out.print(\"A\");\n    case 2: System.out.print(\"B\"); break;\n    case 3: System.out.print(\"C\");\n}",
                "What is the output?",
                "A", "B", "AB", "ABC", "C",
                "Without a 'break' statement after case 1, execution falls through to case 2 before hitting the break. This is called 'fall-through' behavior in switch statements."),
            arrayOf("2",
                "for (int i = 0; i < 5; i++) {\n    if (i % 2 == 0) continue;\n    System.out.print(i);\n}",
                "What is the output?",
                "01234", "024", "13", "135", "C",
                "The 'continue' statement skips the rest of the loop body for the current iteration. Think about which values of i are divisible by 2 (even) and will be skipped."),
            arrayOf("2",
                "int x = 5;\nSystem.out.println(x++ + ++x);",
                "What is the output?",
                "10", "11", "12", "13", "C",
                "x++ uses x's current value THEN increments. ++x increments FIRST then uses the value. Track x step by step: first operand uses x=5 (post-increment), then x becomes 6, then ++x makes it 7."),
            arrayOf("2",
                "int x = 10;\nint y = (x > 5) ? (x < 15 ? 1 : 2) : 3;\nSystem.out.println(y);",
                "What is the output?",
                "1", "2", "3", "10", "A",
                "This is a nested ternary operator. Evaluate the outer condition first, then the inner one. Both conditions involve comparing x=10 to fixed values."),
            arrayOf("3",
                "String s = \"Hello\";\ns.concat(\" World\");\nSystem.out.println(s);",
                "What is the output?",
                "Hello World", "World", "Hello", "NullPointerException", "C",
                "Strings in Java are immutable. The concat() method returns a NEW String but does NOT modify the original. The result is never assigned back to 's'."),
            arrayOf("3",
                "class A {\n    A() { System.out.print(\"A\"); }\n}\nclass B extends A {\n    B() { System.out.print(\"B\"); }\n}\npublic class Main {\n    public static void main(String[] args) {\n        new B();\n    }\n}",
                "What is the output?",
                "B", "A", "AB", "BA", "C",
                "When creating a subclass object, the parent class constructor is always called first (implicitly via super()). Think about the order: parent before child."),
            arrayOf("3",
                "class Counter {\n    static int count = 0;\n    Counter() { count++; }\n}\npublic class Main {\n    public static void main(String[] args) {\n        Counter c1 = new Counter();\n        Counter c2 = new Counter();\n        System.out.println(Counter.count);\n    }\n}",
                "What is the output?",
                "0", "1", "2", "Compilation Error", "C",
                "The 'static' keyword means the variable is shared across ALL instances of the class. Each time a Counter object is created, the shared count is incremented once."),
            arrayOf("4",
                "try {\n    int x = 5 / 0;\n} catch (ArithmeticException e) {\n    System.out.print(\"C\");\n} finally {\n    System.out.print(\"F\");\n}",
                "What is the output?",
                "C", "F", "CF", "FC", "C",
                "The 'finally' block ALWAYS runs after try/catch completes. Division by zero triggers ArithmeticException, so catch runs first, then finally runs after."),
            arrayOf("4",
                "int[] arr = new int[3];\narr[0] = 1;\narr[1] = 2;\narr[2] = 3;\nSystem.out.println(arr[3]);",
                "What is the output?",
                "3", "0", "null", "ArrayIndexOutOfBoundsException", "D",
                "Array indices in Java are zero-based. An array of size 3 has valid indices 0, 1, and 2. Accessing index 3 goes beyond the array bounds."),
            arrayOf("4",
                "public class Main {\n    public static void modify(int[] a) {\n        a[0] = 99;\n    }\n    public static void main(String[] args) {\n        int[] nums = {1, 2, 3};\n        modify(nums);\n        System.out.println(nums[0]);\n    }\n}",
                "What is the output?",
                "1", "99", "Compilation Error", "0", "B",
                "Arrays in Java are reference types. When you pass an array to a method, you pass the reference (memory address), not a copy. Modifying elements inside the method affects the original array.")
        )

        for (q in questions) {
            val values = ContentValues().apply {
                put(COLUMN_LEVEL, q[0].toInt())
                put(COLUMN_SNIPPET, q[1])
                put(COLUMN_QUESTION_TEXT, q[2])
                put(COLUMN_OPTION_A, q[3])
                put(COLUMN_OPTION_B, q[4])
                put(COLUMN_OPTION_C, q[5])
                put(COLUMN_OPTION_D, q[6])
                put(COLUMN_CORRECT_ANSWER, q[7])
                put(COLUMN_HINT, q[8])
            }
            db?.insert(TABLE_QUESTIONS, null, values)
        }
    }

    // --- User Operations ---
    fun registerUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_UNLOCKED_LEVEL, 1)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun loginUser(username: String, password: String): Int {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USER_ID), "$COLUMN_USERNAME=? AND $COLUMN_PASSWORD=?", arrayOf(username, password), null, null, null)
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0)
        }
        cursor.close()
        return userId
    }

    fun getUsernameById(userId: Int): String {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USERNAME), "$COLUMN_USER_ID=?", arrayOf(userId.toString()), null, null, null)
        var username = ""
        if (cursor.moveToFirst()) {
            username = cursor.getString(0)
        }
        cursor.close()
        return username
    }

    fun getUnlockedLevel(userId: Int): Int {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_UNLOCKED_LEVEL), "$COLUMN_USER_ID=?", arrayOf(userId.toString()), null, null, null)
        var level = 1
        if (cursor.moveToFirst()) {
            level = cursor.getInt(0)
        }
        cursor.close()
        return level
    }

    fun updateUnlockedLevel(userId: Int, level: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_UNLOCKED_LEVEL, level)
        }
        db.update(TABLE_USERS, values, "$COLUMN_USER_ID=?", arrayOf(userId.toString()))
    }

    // --- Question Operations ---
    fun getQuestionsForLevel(level: Int): List<Question> {
        val list = mutableListOf<Question>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_QUESTIONS, null, "$COLUMN_LEVEL=?", arrayOf(level.toString()), null, null, null)
        while (cursor.moveToNext()) {
            list.add(Question(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9)
            ))
        }
        cursor.close()
        return list
    }

    // --- History Operations ---
    fun addHistoryRecord(userId: Int, levelName: String, levelNumber: Int, score: Int, status: String, date: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID_FK, userId)
            put(COLUMN_LEVEL_NAME, levelName)
            put(COLUMN_LEVEL_NUMBER, levelNumber)
            put(COLUMN_SCORE_PERCENTAGE, score)
            put(COLUMN_STATUS, status)
            put(COLUMN_DATE, date)
        }
        db.insert(TABLE_HISTORY, null, values)
    }

    fun getHistoryRecords(userId: Int): List<HistoryRecord> {
        val list = mutableListOf<HistoryRecord>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_HISTORY, null, "$COLUMN_USER_ID_FK=?", arrayOf(userId.toString()), null, null, "$COLUMN_HISTORY_ID DESC")
        while (cursor.moveToNext()) {
            list.add(HistoryRecord(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getInt(4),
                cursor.getString(5),
                cursor.getString(6)
            ))
        }
        cursor.close()
        return list
    }

    fun clearHistory(userId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_HISTORY, "$COLUMN_USER_ID_FK=?", arrayOf(userId.toString()))
    }
}

data class Question(
    val id: Int,
    val level: Int,
    val snippet: String,
    val text: String,
    val opA: String,
    val opB: String,
    val opC: String,
    val opD: String,
    val correct: String,
    val hint: String
)

data class HistoryRecord(
    val id: Int,
    val userId: Int,
    val levelName: String,
    val levelNumber: Int,
    val score: Int,
    val status: String,
    val date: String
)
