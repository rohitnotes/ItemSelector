package com.tonyyang.common.itemselector.member.selector

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.tonyyang.common.itemselector.database.AppDatabase
import com.tonyyang.common.itemselector.database.Member
import com.tonyyang.common.itemselector.database.dao.MemberDao

/**
 * @author tonyyang
 */

class MemberSelectorRepository(val application: Application) {

    private val mMemberDao by lazy {
        AppDatabase.getInstance().memberDao()
    }

    private val mAllMembers: LiveData<List<Member>> by lazy {
        mMemberDao.getAllMembers()
    }

    fun getAllMember(): LiveData<List<Member>> {
        return mAllMembers
    }

    fun insert(member: Member) {
        InsertAsyncTask(mMemberDao).execute(member)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: MemberDao) :
        AsyncTask<Member, Void, Void>() {

        override fun doInBackground(vararg params: Member): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

}