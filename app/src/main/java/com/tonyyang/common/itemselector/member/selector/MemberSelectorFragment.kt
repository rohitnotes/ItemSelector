package com.tonyyang.common.itemselector.member.selector

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tonyyang.common.itemselector.database.Member
import com.tonyyang.common.itemselector.R
import com.tonyyang.common.itemselector.member.NewMemberActivity
import com.tonyyang.common.itemselector.util.TestUtils
import kotlinx.android.synthetic.main.fragment_select_member.*
import com.tonyyang.common.itemselector.SeparatorDecoration


/**
 * @author tonyyang
 */

class MemberSelectorFragment: Fragment() {

    companion object {
        private const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
        fun newInstance() = MemberSelectorFragment()
    }

    private val mAdapter by lazy {
        val adapter = MemberSelectorAdapter(activity as Context)
        adapter.showHeader(true)
        adapter
    }

    private val mMemberViewModel by lazy {
        ViewModelProviders.of(this).get(MemberSelectorViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_member, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        search_view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO: implementing keyword filtering
            }
        })

        recyclerView.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SeparatorDecoration.Builder(context)
                .setMargin(16F, 0F)
                .setHeaderCount(1)
                .build())
        }

        mMemberViewModel.getAllMembers().observe(this, Observer<List<Member>> {
            // Update the cached copy of the members in the adapter
            mAdapter.update(it)
        })

        fab.setOnClickListener {
            val intent = Intent(activity, NewMemberActivity::class.java)
            startActivityForResult(intent,
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
            mMemberViewModel.insert(member)
        } else {
            Toast.makeText(activity, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }
}