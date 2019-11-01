package com.tonyyang.common.itemselector.member.selector

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.IllegalStateException
import com.nostra13.universalimageloader.core.ImageLoader
import android.util.SparseBooleanArray
import android.widget.*
import com.tonyyang.common.itemselector.database.Member
import com.tonyyang.common.itemselector.R
import java.util.*
import kotlin.collections.ArrayList


class MemberSelectorAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var mHeaderCnt = 0

    private val isHeaderView: (Int) -> Boolean = { position ->
        mHeaderCnt != 0 && position < mHeaderCnt
    }

    private val realPosition: (Int) -> Int = { position ->
        position - mHeaderCnt
    }

    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    private val mLock = Any()

    private var mLastFilterConstraint = ""

    private val originList = mutableListOf<Member>()

    private var filteredList = mutableListOf<Member>()

    private val itemStateArray = SparseBooleanArray()

    private enum class ItemType(val value: Int) {
        HEADER(0),
        MEMBER(1)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val defaultCellLayout: ConstraintLayout = itemView.findViewById(R.id.default_cell)
        val defaultCellImage: ImageView = itemView.findViewById(R.id.default_cell_image)
        val defaultCellText: TextView = itemView.findViewById(R.id.default_cell_text)
        val defaultCellCheckBox: CheckBox = itemView.findViewById(R.id.default_cell_checkbox)
    }

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val stateView: CheckBox = itemView.findViewById(R.id.stateView)
    }

    fun showHeader(show: Boolean) {
        mHeaderCnt = if (show) 1 else 0
    }

    fun update(memberList: List<Member>) {
        synchronized(mLock) {
            originList.apply {
                clear()
                addAll(memberList)
            }
        }

        notifyAdapterDataSetChanged()
    }

    private fun getItem(position: Int): Member {
        return filteredList[realPosition(position)]
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
            ItemType.HEADER.value -> HeaderViewHolder(
                mInflater.inflate(R.layout.recyclerview_header, parent, false)
            )
            ItemType.MEMBER.value -> MemberViewHolder(
                mInflater.inflate(R.layout.recyclerview_item, parent, false)
            )
            else -> throw IllegalStateException("Illegal ItemType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.defaultCellLayout.setOnClickListener {
                holder.defaultCellCheckBox.isChecked = holder.defaultCellCheckBox.isChecked.not()
            }
            holder.defaultCellImage.setImageResource(R.drawable.ic_multiple_users_silhouette)
            holder.defaultCellText.text = context.getString(R.string.selector_header_text)
        } else if (holder is MemberViewHolder) {
            val member = getItem(position)
            ImageLoader.getInstance().displayImage(member.photoPath, holder.image)
            holder.stateView.isChecked = itemStateArray.get(position)
            holder.title.text = member.displayName
            holder.itemView.setOnClickListener {
                switchState(position, holder.stateView)
            }
        }
    }

    private fun switchState(position: Int, stateView: CheckBox) {
        val changeState = !itemStateArray.get(position, false)
        stateView.isChecked = changeState
        itemStateArray.put(position, changeState)
    }

    private fun notifyAdapterDataSetChanged() {
        if (mLastFilterConstraint.isNotEmpty()) {
            filter.filter(mLastFilterConstraint)
        } else {
            synchronized(mLock) {
                filteredList = ArrayList(originList)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = mHeaderCnt + filteredList.size

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charString = constraint.toString().also {
                mLastFilterConstraint = it
            }
            filteredList = if (charString.isEmpty()) {
                originList
            } else {
                mutableListOf<Member>().apply {
                    originList.forEach {
                        if (it.displayName.toLowerCase(Locale.getDefault()).contains(charString)) {
                            this.add(it)
                        }
                    }
                }
            }
            return FilterResults().apply {
                values = filteredList
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            @Suppress("UNCHECKED_CAST")
            filteredList = results?.values as MutableList<Member>

            notifyDataSetChanged()
        }
    }
}