package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.model.commentModel
import com.google.firebase.database.*

class Shop_commentpreview : AppCompatActivity() {

    //declaire variable
    private lateinit var btnAddComment: Button
    private lateinit var cmtRecycleView: RecyclerView
    private lateinit var cmtList: ArrayList<commentModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shop_commentpreview)

        btnAddComment = findViewById(R.id.addComment)

        cmtRecycleView = findViewById(R.id.rvscomments)
        cmtRecycleView.layoutManager = LinearLayoutManager(this)
        cmtRecycleView.setHasFixedSize(true)

        btnAddComment.setOnClickListener {
            val intent = Intent(this, addComment::class.java)
            startActivity(intent)
        }

        cmtList = arrayListOf<commentModel>()

        getComments()

    }

    private fun getComments() {

        dbRef = FirebaseDatabase.getInstance().getReference("comments")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cmtList.clear()
                if (snapshot.exists()) {
                    for (cmtSnap in snapshot.children) {
                        val cmtData = cmtSnap.getValue(commentModel::class.java)
                        cmtList.add(cmtData!!)
                    }
                    val cAdapter = CommentAdapter(cmtList)
                    cmtRecycleView.adapter = cAdapter

                    cmtRecycleView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}