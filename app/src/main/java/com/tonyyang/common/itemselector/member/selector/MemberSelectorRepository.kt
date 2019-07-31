package com.tonyyang.common.itemselector.member.selector

import androidx.lifecycle.LiveData
import com.tonyyang.common.itemselector.database.AppDatabase
import com.tonyyang.common.itemselector.database.Member


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