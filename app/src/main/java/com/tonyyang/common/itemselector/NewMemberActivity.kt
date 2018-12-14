package com.tonyyang.common.itemselector

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.content.Intent


/**
 * @author tonyyang
 */
class NewMemberActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REPLY_UID = "com.tonyyang.common.itemselector.memberlistsql.REPLY_UID"
        const val EXTRA_REPLY_TID = "com.tonyyang.common.itemselector.memberlistsql.REPLY_TID"
        const val EXTRA_REPLY_DISPLAY_NAME = "com.tonyyang.common.itemselector.memberlistsql.REPLY_DISPLAY_NAME"
        const val EXTRA_REPLY_ALIAS = "com.tonyyang.common.itemselector.memberlistsql.REPLY_ALIAS"
    }

    private val uidET by lazy {
        findViewById<EditText>(R.id.uid)
    }

    private val tidET by lazy {
        findViewById<EditText>(R.id.tid)
    }

    private val displayNameET by lazy {
        findViewById<EditText>(R.id.displayname)
    }

    private val aliasET by lazy {
        findViewById<EditText>(R.id.alias)
    }

    private val saveBtn by lazy {
        findViewById<Button>(R.id.save)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_member)

        saveBtn.setOnClickListener {
            val replyIntent = Intent()
            if (uidET.text.isNullOrBlank() && tidET.text.isNullOrBlank()
                && displayNameET.text.isNullOrBlank() && aliasET.text.isNullOrBlank()
            ) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_UID, uidET.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_TID, tidET.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DISPLAY_NAME, displayNameET.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_ALIAS, aliasET.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
}