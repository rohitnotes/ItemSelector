package com.tonyyang.common.itemselector

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.arch.persistence.db.SupportSQLiteDatabase
import android.os.AsyncTask
import java.security.SecureRandom

/**
 * @author tonyyang
 */

@Database(entities = [Member::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao

    companion object {
        private const val DB_NAME = "app.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, DB_NAME
                    ).addCallback(sRoomDatabaseCallback).build()
                }
            }
            return INSTANCE!!
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                PopulateDbFakeDataAsync(INSTANCE).execute()
            }
        }
    }

    private class PopulateDbFakeDataAsync internal constructor(db: AppDatabase?) : AsyncTask<Void, Void, Void>() {

        private val mDao: MemberDao? = db?.memberDao()

        override fun doInBackground(vararg params: Void): Void? {
            mDao?.deleteAll()
            for (i in 0 until 100) {
                mDao?.insert(
                    Member(
                        getRandomLetter(10),
                        getRandomLetter(8),
                        "Tony $i",
                        "Tony boy $i"
                    )
                )
            }
            return null
        }

        private fun getRandomLetter(length: Int): String {
            val secureRandom = SecureRandom()
            val s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
            val generatedString = StringBuilder()
            for (i in 0 until length) {
                val randomSequence = secureRandom.nextInt(s.length)
                generatedString.append(s[randomSequence])
            }
            return generatedString.toString().toLowerCase()
        }
    }
}