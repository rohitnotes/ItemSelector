package com.tonyyang.common.itemselector

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
    }

    private val mRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    private val mAdapter by lazy {
        val adapter = MemberListAdapter(this)
        adapter.showHeader(true)
        adapter
    }

    private val mMemberViewModel by lazy {
        ViewModelProviders.of(this).get(MemberViewModel::class.java)
    }

    private val mFab by lazy {
        findViewById<View>(R.id.fab)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        mMemberViewModel.getAllMembers().observe(this, Observer<List<Member>> {
            // Update the cached copy of the members in the adapter
            mAdapter.update(it)
        })

        mFab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewMemberActivity::class.java)
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
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }
}
