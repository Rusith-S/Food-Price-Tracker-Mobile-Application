package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.commentModel

class CommentManagerAdapter(private val userCmtList: ArrayList<commentModel>) :
    RecyclerView.Adapter<CommentManagerAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentManagerAdapter.ViewHolder {
        val itemView2 =
            LayoutInflater.from(parent.context).inflate(R.layout.shop_comment_list, parent, false)
        return ViewHolder(itemView2, mListener)
    }

    override fun onBindViewHolder(holder: CommentManagerAdapter.ViewHolder, position: Int) {
        val currentCmt = userCmtList[position]
        holder.tvUCmt.text = currentCmt.newComment
    }


    override fun getItemCount(): Int {
        return userCmtList.size
    }

    class ViewHolder(itemView2: View, clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView2) {
        val tvUCmt: TextView = itemView2.findViewById(R.id.userComment)

        init {
            itemView2.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }


}