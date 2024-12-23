package com.example.oora

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

class add_cart : AppCompatActivity() {
    private lateinit var Database: DatabaseReference
    private lateinit var itemlist:MutableList<cartitem>
    private lateinit var adapterforcart: Adapterforcart
    private  var total: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cart)
        supportActionBar?.setTitle("A PRADHAN PRODUCT")

        val currentuser = FirebaseAuth.getInstance().currentUser?.uid



        Database = FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("cart").child("${currentuser}")
        val totalAmountTextView = findViewById<TextView>(R.id.textView8)

        val recycleview1 =findViewById<RecyclerView>(R.id.recyclerView)
        itemlist = mutableListOf()
        adapterforcart = Adapterforcart(itemlist){ totalAmount ->
            total = totalAmount.toInt()
            totalAmountTextView.text = " $totalAmount"

        }
        recycleview1.layoutManager = LinearLayoutManager(this)
        recycleview1.adapter = adapterforcart

        loadcart()


        val buybutton =findViewById<Button>(R.id.button6)
        buybutton.setOnClickListener {

            val intent = Intent(this,checkout::class.java)
            intent.putExtra("total",total)
            startActivity(intent)
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigatio)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homemenu -> {
                    startActivity(Intent(this, Mainpage::class.java))
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

    private fun loadcart() {
        Database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemlist.clear()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(cartitem::class.java)
//                    post?.let { itemlist.add(it) }
                    if (post != null) {
                        itemlist.add(post)
                    }

                }
                adapterforcart.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}