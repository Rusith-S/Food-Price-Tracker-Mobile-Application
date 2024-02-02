package com.example.userlogin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userlogin.R
import com.example.userlogin.adapters.ItemAdapters
import com.example.userlogin.models.ItemModel
import com.google.firebase.database.*

class view_items : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData:TextView
    private lateinit var itemList: ArrayList<ItemModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_items)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        itemList = arrayListOf<ItemModel>()

        getItemdata()

    }

    private fun getItemdata(){
        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Items")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                if (snapshot.exists()){
                    for (ItemSnap in snapshot.children){
                        val Itemdata = ItemSnap.getValue(ItemModel::class.java)
                        itemList.add(Itemdata!!)
                    }
                    val mAdapter = ItemAdapters(itemList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ItemAdapters.onItemClickListener {
                        override fun onItemClick(position: Int) {
                           val intent = Intent(this@view_items, ItemDetails::class.java)

                            intent.putExtra("itemId", itemList[position].itemId)
                            intent.putExtra("itemName", itemList[position].itemName)
                            intent.putExtra("itemPrice", itemList[position].itemPrice)
                            intent.putExtra("outOrIn", itemList[position].outOrIn)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}