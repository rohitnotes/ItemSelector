package com.tonyyang.common.itemselector

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_select_member.*

/**
 * @author tonyyang
 */

class SelectMemberFragment: Fragment() {

    companion object {
        private const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
        fun newInstance() = SelectMemberFragment()
    }

    private val mAdapter by lazy {
        val adapter = MemberListAdapter(activity as Context)
        adapter.showHeader(true)
        adapter
    }

    private val mMemberViewModel by lazy {
        ViewModelProviders.of(this).get(MemberViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_member, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        search_view.setOnEditorActionListener { v, _, _ ->
            val keyword = v.text
            // TODO: implementing keyword filtering
            false
        }

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        mMemberViewModel.getAllMembers().observe(this, Observer<List<Member>> {
            // Update the cached copy of the members in the adapter
            mAdapter.update(it)
        })

        fab.setOnClickListener {
            val intent = Intent(activity, NewMemberActivity::class.java)
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val photoPath = data?.getStringExtra(NewMemberActivity.EXTRA_PHOTO_PATH) ?: ""
            val displayName = data?.getStringExtra(NewMemberActivity.EXTRA_REPLY_DISPLAY_NAME)
            val alias = data?.getStringExtra(NewMemberActivity.EXTRA_REPLY_ALIAS)
            val member = Member(
                    Utils.getRandomLetter(10),
                    Utils.getRandomLetter(8),
                    displayName.toString(),
                    alias.toString(),
                    photoPath)
            mMemberViewModel.insert(member)
        } else {
            Toast.makeText(activity, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }
}