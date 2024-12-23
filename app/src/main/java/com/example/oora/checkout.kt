package com.example.oora

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class checkout : AppCompatActivity() {

    private lateinit var Database: DatabaseReference
    private lateinit var itemlist:MutableList<checkoutitem>
    private lateinit var Adapterforcheckput :checkoutAdapter
    private lateinit var databasefororder:DatabaseReference
    private lateinit var  dataa:DatabaseReference
    private lateinit var databaseforordermanager: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        supportActionBar?.setTitle("A PRADHAN PRODUCT")

        val currentuser = FirebaseAuth.getInstance().currentUser?.uid
        Database = FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("cart").child("${currentuser}")

        databasefororder =FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("orderdetails").child("${currentuser}")

        databaseforordermanager =FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("orderformanager").child("${currentuser}")

        dataa =FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("userinformation").child("${currentuser}")

        val recycel =findViewById<RecyclerView>(R.id.recyclerView2)

        itemlist = mutableListOf()
        Adapterforcheckput =checkoutAdapter(itemlist)
        recycel.adapter =Adapterforcheckput
        recycel.layoutManager =LinearLayoutManager(this)
        loaddata()

        val namee =findViewById<EditText>(R.id.editTextText)
        val numberr =findViewById<EditText>(R.id.editTextPhone)
        val pincodee =findViewById<EditText>(R.id.editTextNumber4)
        val addresss =findViewById<EditText>(R.id.editTextText2)
        val placeOrder =findViewById<Button>(R.id.button8)
        val pro =findViewById<ProgressBar>(R.id.progressBar4)
        val totaltext =findViewById<TextView>(R.id.textView16)

        val total = intent.getIntExtra("total",0)
        totaltext.text ="Total Amount : $total"



    placeOrder.setOnClickListener {
        pro.visibility = View.VISIBLE
        val ord =databasefororder
        val namer =namee.text.toString()
        val number =numberr.text.toString()
        val pincode =pincodee.text.toString()
        val address =addresss.text.toString()

        val orderid =databasefororder.push().key
        val tim =System.currentTimeMillis()
        val rrr =databasefororder.child("${tim}")

        val data = hashMapOf(
            "name" to namer,
            "number" to number,
            "pincode" to pincode,
            "address" to address,
        )
        if(namer.isNotEmpty() && number.isNotEmpty() && pincode.isNotEmpty() && address.isNotEmpty()){

            dataa.setValue(data).addOnSuccessListener {
                rrr.child("order").setValue(itemlist).addOnSuccessListener {
                    databaseforordermanager.child("${tim}").setValue(itemlist).addOnSuccessListener {
                        pro.visibility = View.GONE
                        Toast.makeText(this,"order placed",Toast.LENGTH_SHORT).show()
                        val intent =Intent(thsi@this,Mainpage::class.java)
                        startActivity(intent)
                    }
                }


            };if(namer.isEmpty() || number.isEmpty() || pincode.isEmpty() || address.isEmpty()){
                Toast.makeText(this,"please fill all the details",Toast.LENGTH_SHORT).show()
                pro.visibility = View.GONE


            }
        }
    }
                dataa.get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("name").value
                val number = it.child("number").value
                val pin = it.child("pincode").value
                val add = it.child("address").value


                namee.setText(name.toString())
                numberr.setText(number.toString())
                pincodee.setText(pin.toString())
                addresss.setText(add.toString())

            }


        }






    }

    private fun loaddata() {
        Database.addValueEventListener(object : ValueEventListener {
//            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                itemlist.clear()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(checkoutitem::class.java)
                    post?.let { itemlist.add(it) }

                }
                Adapterforcheckput.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }


}