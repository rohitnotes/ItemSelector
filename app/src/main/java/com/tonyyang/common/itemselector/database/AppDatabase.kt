package com.tonyyang.common.itemselector.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tonyyang.common.itemselector.CoreApplication
import com.tonyyang.common.itemselector.R
import com.tonyyang.common.itemselector.database.dao.MemberDao
import com.tonyyang.common.itemselector.util.TestUtils
import com.tonyyang.common.itemselector.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random


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
                GlobalScope.launch(Dispatchers.IO) {
                    val memberDao = INSTANCE.memberDao()
                    memberDao.deleteAll()
                    val drawables = intArrayOf(
                        R.drawable.ic_035_user_34,
                        R.drawable.ic_004_user_3
                    )
                    val list = mutableListOf<Member>()
                    for (i in 0 until 100) {
                        list.add(Member(
                            TestUtils.getRandomLetter(10),
                            TestUtils.getRandomLetter(8),
                            "Tony " + (i + 1),
                            "Tony boy " + (i + 1),
                            ImageUtils.getResourceDrawableUri(drawables[Random.nextInt(0, 2)]).toString()
                        ))
                    }
                    memberDao.insertAll(list)
                }
            }
        }

        fun getInstance() = INSTANCE
    }
}