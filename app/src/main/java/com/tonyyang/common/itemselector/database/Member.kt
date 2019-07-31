package com.tonyyang.common.itemselector.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tonyyang.common.itemselector.database.Member.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class Member(
    @ColumnInfo(name = UID) var uid: String,
    @ColumnInfo(name = TID) var tid: String,
    @ColumnInfo(name = DISPLAY_NAME) var displayName: String,
    @ColumnInfo(name = ALIAS) var alias: String,
    @ColumnInfo(name = PHOTO_PATH) var photoPath: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    companion object {
        const val TABLE_NAME = "member"
        const val UID = "uid"
        const val TID = "tid"
        const val DISPLAY_NAME = "display_name"
        const val ALIAS = "alias"
        const val PHOTO_PATH = "photo_path"
    }
}