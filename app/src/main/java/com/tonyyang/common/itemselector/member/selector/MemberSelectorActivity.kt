package com.tonyyang.common.itemselector.member.selector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tonyyang.common.itemselector.R

class MemberSelectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        MemberSelectorFragment.newInstance()
                    )
                    .commitNow()
        }
    }
}
