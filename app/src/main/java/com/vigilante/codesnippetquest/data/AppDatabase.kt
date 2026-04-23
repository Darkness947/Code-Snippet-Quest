package com.vigilante.codesnippetquest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Question::class, HistoryRecord::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "CodeSnippetQuest.db"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.appDao())
                }
            }
        }

        suspend fun populateDatabase(dao: AppDao) {
            val rawQuestions = listOf(
                arrayOf("1", "System.out.println(10 + 20 + \"Java\" + 10 + 20);", "What is the output of the code snippet above?", "ما هو ناتج مقتطف الكود أعلاه؟", "30Java30", "1020Java1020", "30Java1020", "Compilation Error", "C", "Java evaluates expressions left to right. Before a String is encountered, integers are added. After a String, + becomes concatenation."),
                arrayOf("1", "String s1 = \"Code\";\nString s2 = new String(\"Code\");\nSystem.out.println(s1 == s2);", "What is the output?", "ما هو الناتج؟", "true", "false", "Compilation Error", "Runtime Exception", "B", "The == operator compares object references, not content. String literals use the string pool, but 'new String()' always creates a new object on the heap."),
                arrayOf("1", "int x = 1;\nswitch(x) {\n    case 1: System.out.print(\"A\");\n    case 2: System.out.print(\"B\"); break;\n    case 3: System.out.print(\"C\");\n}", "What is the output?", "ما هو الناتج؟", "A", "B", "AB", "ABC", "C", "Without a 'break' statement after case 1, execution falls through to case 2 before hitting the break. This is called 'fall-through' behavior in switch statements."),
                arrayOf("2", "for (int i = 0; i < 5; i++) {\n    if (i % 2 == 0) continue;\n    System.out.print(i);\n}", "What is the output?", "ما هو الناتج؟", "01234", "024", "13", "135", "C", "The 'continue' statement skips the rest of the loop body for the current iteration. Think about which values of i are divisible by 2 (even) and will be skipped."),
                arrayOf("2", "int x = 5;\nSystem.out.println(x++ + ++x);", "What is the output?", "ما هو الناتج؟", "10", "11", "12", "13", "C", "x++ uses x's current value THEN increments. ++x increments FIRST then uses the value. Track x step by step: first operand uses x=5 (post-increment), then x becomes 6, then ++x makes it 7."),
                arrayOf("2", "int x = 10;\nint y = (x > 5) ? (x < 15 ? 1 : 2) : 3;\nSystem.out.println(y);", "What is the output?", "ما هو الناتج؟", "1", "2", "3", "10", "A", "This is a nested ternary operator. Evaluate the outer condition first, then the inner one. Both conditions involve comparing x=10 to fixed values."),
                arrayOf("3", "String s = \"Hello\";\ns.concat(\" World\");\nSystem.out.println(s);", "What is the output?", "ما هو الناتج؟", "Hello World", "World", "Hello", "NullPointerException", "C", "Strings in Java are immutable. The concat() method returns a NEW String but does NOT modify the original. The result is never assigned back to 's'."),
                arrayOf("3", "class A {\n    A() { System.out.print(\"A\"); }\n}\nclass B extends A {\n    B() { System.out.print(\"B\"); }\n}\npublic class Main {\n    public static void main(String[] args) {\n        new B();\n    }\n}", "What is the output?", "ما هو الناتج؟", "B", "A", "AB", "BA", "C", "When creating a subclass object, the parent class constructor is always called first (implicitly via super()). Think about the order: parent before child."),
                arrayOf("3", "class Counter {\n    static int count = 0;\n    Counter() { count++; }\n}\npublic class Main {\n    public static void main(String[] args) {\n        Counter c1 = new Counter();\n        Counter c2 = new Counter();\n        System.out.println(Counter.count);\n    }\n}", "What is the output?", "ما هو الناتج؟", "0", "1", "2", "Compilation Error", "C", "The 'static' keyword means the variable is shared across ALL instances of the class. Each time a Counter object is created, the shared count is incremented once."),
                arrayOf("4", "try {\n    int x = 5 / 0;\n} catch (ArithmeticException e) {\n    System.out.print(\"C\");\n} finally {\n    System.out.print(\"F\");\n}", "What is the output?", "ما هو الناتج؟", "C", "F", "CF", "FC", "C", "The 'finally' block ALWAYS runs after try/catch completes. Division by zero triggers ArithmeticException, so catch runs first, then finally runs after."),
                arrayOf("4", "int[] arr = new int[3];\narr[0] = 1;\narr[1] = 2;\narr[2] = 3;\nSystem.out.println(arr[3]);", "What is the output?", "ما هو الناتج؟", "3", "0", "null", "ArrayIndexOutOfBoundsException", "D", "Array indices in Java are zero-based. An array of size 3 has valid indices 0, 1, and 2. Accessing index 3 goes beyond the array bounds."),
                arrayOf("4", "public class Main {\n    public static void modify(int[] a) {\n        a[0] = 99;\n    }\n    public static void main(String[] args) {\n        int[] nums = {1, 2, 3};\n        modify(nums);\n        System.out.println(nums[0]);\n    }\n}", "What is the output?", "ما هو الناتج؟", "1", "99", "Compilation Error", "0", "B", "Arrays in Java are reference types. When you pass an array to a method, you pass the reference (memory address), not a copy. Modifying elements inside the method affects the original array.")
            )

            val questionsToInsert = rawQuestions.map { q ->
                Question(
                    level = q[0].toInt(),
                    snippet = q[1],
                    text = q[2],
                    textAr = q[3],
                    opA = q[4],
                    opB = q[5],
                    opC = q[6],
                    opD = q[7],
                    correct = q[8],
                    hint = q[9]
                )
            }
            dao.insertQuestions(questionsToInsert)
        }
    }
}
