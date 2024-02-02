package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentManagerAdapter
import com.example.myapplication.model.commentModel
import com.google.firebase.database.*

class manageReview : AppCompatActivity() {


    //declair variable for RecycleView, ArrayList of commants, and database ref
    private lateinit var userCmtRecycleView: RecyclerView
    private lateinit var userCmtList: ArrayList<commentModel>
    private lateinit var dbURef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_review)


        //Intialize RV and set properties
        userCmtRecycleView = findViewById(R.id.rvucomments)
        userCmtRecycleView.layoutManager = LinearLayoutManager(this)
        userCmtRecycleView.setHasFixedSize(true)

        //set arrayList for comments
        userCmtList = arrayListOf<commentModel>()

        //call funtions to get comments from db
        getUserComments()
    }

    private fun getUserComments() {

        //get ref to db
        dbURef = FirebaseDatabase.getInstance().getReference("comments")

        //attach valueEventLister to retrive commentss from db
        dbURef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear arrayList of comments
                userCmtList.clear()

                //if comments are exist in the db retrive them and add them to arrayList
                if (snapshot.exists()) {
                    for (ucmtSnap in snapshot.children) {
                        val userCmtData = ucmtSnap.getValue(commentModel::class.java)
                        userCmtList.add(userCmtData!!)
                    }
                    //intialize CommenetManagerAdapter with arraylist of comments
                    val ucAdapter = CommentManagerAdapter(userCmtList)
                    userCmtRecycleView.adapter = ucAdapter

                    //set click lister for RecycleView items
                    ucAdapter.setOnItemClickListener(object :
                        CommentManagerAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            //when a RV item clicked open edit_Review activity
                            val intent = Intent(this@manageReview, Edit_Review::class.java)

                            //pass commentId and comment to editReview activity
                            intent.putExtra("commentId", userCmtList[position].commentId)
                            intent.putExtra("newComment", userCmtList[position].newComment)
                            startActivity(intent)
                        }

                    })
                    //make RV Visible
                    userCmtRecycleView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}