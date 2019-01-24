package com.tonyyang.common.itemselector.member

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.widget.ImageView
import com.nostra13.universalimageloader.core.ImageLoader
import com.tonyyang.common.itemselector.R


/**
 * @author tonyyang
 */
class NewMemberActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val EXTRA_PHOTO_PATH = "com.tonyyang.common.itemselector.memberlistsql.PHOTO_PATH"
        const val EXTRA_REPLY_DISPLAY_NAME = "com.tonyyang.common.itemselector.memberlistsql.REPLY_DISPLAY_NAME"
        const val EXTRA_REPLY_ALIAS = "com.tonyyang.common.itemselector.memberlistsql.REPLY_ALIAS"
    }

    private val uploadPhotoIv by lazy {
        findViewById<ImageView>(R.id.upload_photo)
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

    private var photoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_member)
        uploadPhotoIv.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                REQUEST_CODE_PICK_IMAGE
            )
        }
        saveBtn.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(EXTRA_PHOTO_PATH, photoPath)
            replyIntent.putExtra(EXTRA_REPLY_DISPLAY_NAME, displayNameET.text.toString())
            replyIntent.putExtra(EXTRA_REPLY_ALIAS, aliasET.text.toString())
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            photoPath = uri?.toString()
            ImageLoader.getInstance().displayImage(uri.toString(), uploadPhotoIv)
        }
    }
}