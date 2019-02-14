package com.tonyyang.common.itemselector.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.db.SupportSQLiteDatabase
import android.os.AsyncTask
import com.tonyyang.common.itemselector.CoreApplication
import com.tonyyang.common.itemselector.R
import com.tonyyang.common.itemselector.database.dao.MemberDao
import com.tonyyang.common.itemselector.util.TestUtils
import com.tonyyang.common.itemselector.util.ImageUtils
import kotlin.random.Random

/**
 * @author tonyyang
 */

@Database(entities = [Member::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao

    companion object {
        private const val DB_NAME = "app.db"

        private val INSTANCE: AppDatabase by lazy {
            Room.databaseBuilder(
                CoreApplication.getContext(),
                AppDatabase::class.java,
                DB_NAME
            ).addCallback(sRoomDatabaseCallback).build()
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                PopulateDbFakeDataAsync(INSTANCE).execute()
            }
        }

        fun getInstance() = INSTANCE
    }

    private class PopulateDbFakeDataAsync internal constructor(db: AppDatabase) : AsyncTask<Void, Void, Void>() {

        private val mDao: MemberDao = db.memberDao()

        override fun doInBackground(vararg params: Void): Void? {
            mDao.deleteAll()
            val drawables = intArrayOf(
                R.drawable.ic_035_user_34,
                R.drawable.ic_004_user_3
            )
            for (i in 0 until 100) {
                mDao.insert(
                    Member(
                        TestUtils.getRandomLetter(10),
                        TestUtils.getRandomLetter(8),
                        "Tony " + (i + 1),
                        "Tony boy " + (i + 1),
                        ImageUtils.getResourceDrawableUri(drawables[Random.nextInt(0, 2)]).toString()
                    )
                )
            }
            return null
        }
    }
}