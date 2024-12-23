package com.example.oora

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Profile : AppCompatActivity() {
    private lateinit var Database: DatabaseReference
    private lateinit var itemlist:MutableList<checkoutitem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.setTitle("A PRADHAN PRODUCT")


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation1)
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

                    true
                }

                else -> false
            }
        }

        val nam = findViewById<TextView>(R.id.textView)
        val phonenumber = findViewById<TextView>(R.id.textView13)
        val pincode = findViewById<TextView>(R.id.textView14)
        val address = findViewById<TextView>(R.id.textView15)
        val orderlist = findViewById<TextView>(R.id.button9)

        val currentuser = FirebaseAuth.getInstance().currentUser?.uid
        Database =
            FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("userinformation").child("${currentuser}")

        Database.get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("name").value
                val number = it.child("number").value
                val pin = it.child("pincode").value
                val add = it.child("address").value

                nam.text = name.toString()
                phonenumber.text = number.toString()
                pincode.text = pin.toString()
                address.text = add.toString()

            }
        }


        val orderhistory = findViewById<TextView>(R.id.button9)
        orderhistory.setOnClickListener {
            val intent =Intent(this,ordertrack::class.java)
            startActivity(intent)
        }


    }
}