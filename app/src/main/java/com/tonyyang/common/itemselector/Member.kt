package com.tonyyang.common.itemselector

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author tonyyang
 */

const val TABLE_NAME = "member"
const val ID = "_id"
const val UID = "uid"
const val TID = "tid"
const val DISPLAY_NAME = "display_name"
const val ALIAS = "alias"

@Entity(tableName = TABLE_NAME)
data class Member(
    @ColumnInfo(name = UID) var uid: String,
    @ColumnInfo(name = TID) var tid: String,
    @ColumnInfo(name = DISPLAY_NAME) var displayName: String,
    @ColumnInfo(name = ALIAS) var alias: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}