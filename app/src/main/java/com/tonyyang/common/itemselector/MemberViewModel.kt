package com.tonyyang.common.itemselector

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

/**
 * @author tonyyang
 */
class MemberViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: MemberRepository by lazy { MemberRepository(application) }

    private val mAllMembers: LiveData<List<Member>> by lazy {
        mRepository.getAllMember()
    }

    fun getAllMembers(): LiveData<List<Member>> {
        return mAllMembers
    }

    fun insert(member: Member) {
        mRepository.insert(member)
    }

}