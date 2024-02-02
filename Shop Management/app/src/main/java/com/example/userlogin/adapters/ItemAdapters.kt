package com.example.userlogin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userlogin.R
import com.example.userlogin.models.ItemModel


class ItemAdapters (private val itemList: ArrayList<ItemModel>):
    RecyclerView.Adapter<ItemAdapters.ViewHolder>(){

    private lateinit var mListener:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_list, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = itemList[position]
        holder.tvEmpName.text = currentEmp.itemName
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val tvEmpName : TextView = itemView.findViewById(R.id.tvItemName)

        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}