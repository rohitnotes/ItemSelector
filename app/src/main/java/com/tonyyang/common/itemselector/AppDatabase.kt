package com.tonyyang.common.itemselector

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.arch.persistence.db.SupportSQLiteDatabase
import android.os.AsyncTask

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
                PopulateDbFakeInfoAsync(INSTANCE).execute()
            }
        }
    }

    private class PopulateDbFakeInfoAsync internal constructor(db: AppDatabase?) : AsyncTask<Void, Void, Void>() {

        private val mDao: MemberDao? = db?.memberDao()

        override fun doInBackground(vararg params: Void): Void? {
            mDao?.deleteAll()
            mDao?.insert(Member("ocs5jfszvy", "x2z2o83q", "Tony", "Tony boy"))
            mDao?.insert(Member("mtky0763xs", "x2z2o83q", "Frank", "Fucky boy"))
            return null
        }
    }
}