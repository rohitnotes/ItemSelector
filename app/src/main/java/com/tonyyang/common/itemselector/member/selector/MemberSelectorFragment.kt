package com.tonyyang.common.itemselector.member.selector

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tonyyang.common.itemselector.R
import com.tonyyang.common.itemselector.SeparatorDecoration
import com.tonyyang.common.itemselector.database.Member
import com.tonyyang.common.itemselector.member.NewMemberActivity
import com.tonyyang.common.itemselector.parcel
import com.tonyyang.common.itemselector.util.TestUtils
import kotlinx.android.synthetic.main.fragment_select_member.*


class MemberSelectorFragment : Fragment() {

    companion object {
        private const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
        fun newInstance() = MemberSelectorFragment()
    }

    private val mAdapter by lazy {
        MemberSelectorAdapter(activity as Context).apply {
            showHeader(true)
        }
    }

    private val mMemberViewModel by lazy {
        ViewModelProviders.of(this).get(MemberSelectorViewModel::class.java)
    }

    private var isAddNewMember = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_member, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        search_view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mAdapter.filter.filter(s)
            }
        })

        recyclerView.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(
                SeparatorDecoration.Builder(context)
                    .setMargin(16F, 0F)
                    .setHeaderCount(1)
                    .build()
            )
        }

        mMemberViewModel.getAllMembers().observe(this, Observer<List<Member>> {
            // Update the cached copy of the members in the adapter
            mAdapter.update(it)
            if (isAddNewMember) {
                recyclerView.smoothScrollToPosition(
                    mMemberViewModel.getAllMembers().value?.size
                        ?.plus(mAdapter.mHeaderCnt)?.minus(1).parcel()
                )
                isAddNewMember = false
            }
        })

        mMemberViewModel.getIsUpdating().observe(this, Observer<Boolean> { isVisible ->
            if (isVisible == true) {
                progress_circle.visibility = View.VISIBLE
            } else {
                progress_circle.visibility = View.GONE
            }
        })

        fab.setOnClickListener {
            val intent = Intent(activity, NewMemberActivity::class.java)
            startActivityForResult(
                intent,
                NEW_WORD_ACTIVITY_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val photoPath = data?.getStringExtra(NewMemberActivity.EXTRA_PHOTO_PATH) ?: ""
            val displayName = data?.getStringExtra(NewMemberActivity.EXTRA_REPLY_DISPLAY_NAME)
            val alias = data?.getStringExtra(NewMemberActivity.EXTRA_REPLY_ALIAS)
            val member = Member(
                TestUtils.getRandomLetter(10),
                TestUtils.getRandomLetter(8),
                displayName.toString(),
                alias.toString(),
                photoPath
            )
            mMemberViewModel.addNewMember(member)
            isAddNewMember = true
        }
    }
}