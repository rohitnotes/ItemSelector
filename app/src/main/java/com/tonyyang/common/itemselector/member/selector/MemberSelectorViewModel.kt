package com.tonyyang.common.itemselector.member.selector

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.tonyyang.common.itemselector.database.Member

/**
 * @author tonyyang
 */
class MemberSelectorViewModel(application: Application) : AndroidViewModel(application) {

    private val mMemberSelectorRepository: MemberSelectorRepository by lazy {
        MemberSelectorRepository(
            application
        )
    }

    private val mAllMembers: LiveData<List<Member>> by lazy {
        mMemberSelectorRepository.getAllMember()
    }

    fun getAllMembers(): LiveData<List<Member>> {
        return mAllMembers
    }

    fun insert(member: Member) {
        mMemberSelectorRepository.insert(member)
    }

}