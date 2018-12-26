package com.tonyyang.common.itemselector

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import java.lang.IllegalStateException
import com.nostra13.universalimageloader.core.ImageLoader
import com.orhanobut.logger.Logger

/**
 * @author tonyyang
 */
class MemberListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mHeaderCnt = 0

    private val isHeaderView: (Int) -> Boolean = { position ->
        mHeaderCnt != 0 && position < mHeaderCnt
    }

    private val getRealPosition: (Int) -> Int = { position ->
        position - mHeaderCnt
    }

    private val reverseVisibility: (Int) -> Int = { visibility ->
        if (visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    private val mMembers = mutableListOf<Member>()

    private enum class ItemType(val value: Int) {
        HEADER(0),
        MEMBER(1)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchView: EditText = itemView.findViewById(R.id.search_view)
        val defaultCellLayout: ConstraintLayout = itemView.findViewById(R.id.default_cell)
        val defaultCellImage: ImageView = itemView.findViewById(R.id.default_cell_image)
        val defaultCellText: TextView = itemView.findViewById(R.id.default_cell_text)
        val defaultCellCheckBox: CheckBox = itemView.findViewById(R.id.default_cell_checkbox)
    }

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val checkView: View = itemView.findViewById(R.id.checkView)
    }

    fun showHeader(show: Boolean) {
        mHeaderCnt = if (show) {
            1
        } else {
            0
        }
    }

    fun update(members: List<Member>?) {
        mMembers.apply {
            clear()
            if (members != null) {
                addAll(members)
            }
        }
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): Member {
        return mMembers[getRealPosition(position)]
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeaderView(position)) {
            ItemType.HEADER.value
        } else {
            ItemType.MEMBER.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemType.HEADER.value -> HeaderViewHolder(mInflater.inflate(R.layout.recyclerview_header, parent, false))
            ItemType.MEMBER.value -> MemberViewHolder(mInflater.inflate(R.layout.recyclerview_item, parent, false))
            else -> throw IllegalStateException("Illegal ItemType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.searchView.setOnEditorActionListener { v, _, _ ->
                val keyword = v.text
                // TODO: implementing keyword filtering
                false
            }
            holder.defaultCellLayout.setOnClickListener {
                holder.defaultCellCheckBox.isChecked = holder.defaultCellCheckBox.isChecked.not()
            }
            holder.defaultCellImage.setImageResource(R.drawable.ic_multiple_users_silhouette)
            holder.defaultCellText.text = context.getString(R.string.selector_header_text)
        } else if (holder is MemberViewHolder) {
            val member = getItem(position)
            Logger.d("photoPath: ".plus(member.photoPath))
            ImageLoader.getInstance().displayImage(member.photoPath, holder.image)
            holder.title.text = member.displayName
            holder.itemView.setOnClickListener {
                val oldVisibilityState = holder.checkView.visibility
                holder.checkView.visibility = reverseVisibility(oldVisibilityState)
            }
        }
    }

    override fun getItemCount(): Int = mHeaderCnt + mMembers.size
}