package com.example.oora

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.checkerframework.common.subtyping.qual.Bottom

class  Mainpage : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var postList: MutableList<ItemViewmodel>
    private lateinit var postAdapter: customAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mainpage)
        supportActionBar?.setTitle("A PRADHAN PRODUCT")




        progressBar=findViewById(R.id.progressBar3)
//        FirebaseService.syncCartWithFeed()

        database = FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("productdetails")


        val recyclerView =findViewById<RecyclerView>(R.id.recyclerview)
        // Initialize RecyclerView
        postList = mutableListOf()
        postAdapter = customAdapter(postList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter


        loadPosts()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homemenu -> {
                    // Stay on current activity (MainActivity)
                    true
                }
                R.id.cartmenu -> {
                    startActivity(Intent(this, add_cart::class.java))
                    true
                }
                R.id.profilemenu -> {
                    startActivity(Intent(this, Profile::class.java))
                    true
                }
                else -> false
            }
        }
    }
    private fun loadPosts() {

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(ItemViewmodel::class.java)
                    post?.let { postList.add(it) }

                }
                postAdapter.notifyDataSetChanged()
                syncCartWithFeed()

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
        private fun syncCartWithFeed() {

            val currentuser = FirebaseAuth.getInstance().currentUser?.uid ?: return
            val cartRef =
                FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("cart").child(currentuser)

            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (productSnapshot in snapshot.children) {
                        val productDetails = productSnapshot.getValue(ItemViewmodel::class.java)
                        val productKey = productSnapshot.key

                        if (productDetails != null && productKey != null) {
                            // Update each corresponding item in the user's cart
                            cartRef.child(productKey)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(cartSnapshot: DataSnapshot) {
                                        if (cartSnapshot.exists()) {
                                            // Update price and description in the cart if it exists
                                            cartRef.child(productKey).child("price")
                                                .setValue(productDetails.price)
                                            cartRef.child(productKey).child("discription")
                                                .setValue(productDetails.discription)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Handle possible errors.
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }

    }



