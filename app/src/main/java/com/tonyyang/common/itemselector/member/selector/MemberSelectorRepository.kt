package com.tonyyang.common.itemselector.member.selector

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tonyyang.common.itemselector.database.AppDatabase
import com.tonyyang.common.itemselector.database.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author tonyyang
 */

object MemberSelectorRepository {

    private val mMemberDao by lazy {
        AppDatabase.getInstance().memberDao()
    }

    private val mAllMembers by lazy {
        mMemberDao.getAllMembers()
    }

    fun getAllMember(): LiveData<List<Member>> {
        return mAllMembers
    }

    fun insert(member: Member) {
        mMemberDao.insert(member)
    }
}