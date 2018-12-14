package com.tonyyang.common.itemselector

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

/**
 * @author tonyyang
 */

class MemberRepository(val application: Application) {

    private val mMemberDao by lazy {
        AppDatabase.getInstance(application).memberDao()
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