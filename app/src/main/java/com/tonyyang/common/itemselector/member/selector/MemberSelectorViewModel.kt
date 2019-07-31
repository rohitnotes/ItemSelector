package com.tonyyang.common.itemselector.member.selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tonyyang.common.itemselector.database.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MemberSelectorViewModel : ViewModel() {

    private val mAllMembers by lazy {
        MemberSelectorRepository.getAllMember()
    }

    private val mIsUpdating by lazy {
        MutableLiveData<Boolean>()
    }

    fun getAllMembers(): LiveData<List<Member>> {
        return mAllMembers
    }

    fun getIsUpdating(): LiveData<Boolean> {
        return mIsUpdating
    }

    fun addNewMember(member: Member) {
        mIsUpdating.value = true
        GlobalScope.launch(Dispatchers.IO) {
            MemberSelectorRepository.insert(member)
            delay(1500)
            mIsUpdating.postValue(false)
        }
    }

}