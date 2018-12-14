package com.tonyyang.common.itemselector

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * @author tonyyang
 */
class MemberListAdapter(context: Context) : RecyclerView.Adapter<MemberListAdapter.MemberViewHolder>() {

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
            private set
    }

    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    private val mMembers = mutableListOf<Member>()

    fun update(members: List<Member>?) {
        with(mMembers) {
            clear()
            if (members != null) {
                addAll(members)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false)
        return MemberViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = mMembers[position]
        holder.title.text = member.displayName
    }

    override fun getItemCount(): Int = mMembers.size
}