package com.example.oora

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Directbuy : AppCompatActivity() {
    private lateinit var Database: DatabaseReference
    private lateinit var itemlist:MutableList<checkoutitem>
    private lateinit var Adapterforcheckput :checkoutAdapter
    private lateinit var databasefororder: DatabaseReference
    private lateinit var  dataa: DatabaseReference
    private lateinit var databaseforordermanager: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directbuy)
        supportActionBar?.setTitle("A PRADHAN PRODUCT")
        itemlist = mutableListOf()

        val currentuser = FirebaseAuth.getInstance().currentUser?.uid
        databasefororder = FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("orderdetails").child("${currentuser}")

        databaseforordermanager = FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("orderformanager").child("${currentuser}")

        dataa = FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("userinformation").child("${currentuser}")

        val imageurl =intent.getStringExtra("imageurl")
        val disc =intent.getStringExtra("discription")
        val price =intent.getStringExtra("price")
        val kg =intent.getStringExtra("kg")


        val image =findViewById<ImageView>(R.id.imageView2)
        val discript =findViewById<TextView>(R.id.textView17)
        val pric =findViewById<TextView>(R.id.textView18)
        val kgs =findViewById<TextView>(R.id.textView19)
        val total =findViewById<TextView>(R.id.textView20)

        imageurl?.let {
            Glide.with(this)
                .load(it)
                .into(image)
        }
        discript.text=disc
        pric.text ="${price}/kg"
        kgs.text="${kg} kg"
        total.text = (price!!.toInt()*kg!!.toInt()).toString()


        // for data fetching

        val namee =findViewById<EditText>(R.id.editTextText)
        val numberr =findViewById<EditText>(R.id.editTextPhone)
        val pincodee =findViewById<EditText>(R.id.editTextNumber4)
        val addresss =findViewById<EditText>(R.id.editTextText2)
        val placeOrder =findViewById<Button>(R.id.button8)
        val pro =findViewById<ProgressBar>(R.id.progressBar4)

        placeOrder.setOnClickListener {
            pro.visibility = View.VISIBLE
            val ord =databasefororder
            val namer =namee.text.toString()
            val number =numberr.text.toString()
            val pincode =pincodee.text.toString()
            val address =addresss.text.toString()

            val imageurl =intent.getStringExtra("imageurl")
            val disc =intent.getStringExtra("discription")
            val price =intent.getStringExtra("price")
            val kg =intent.getStringExtra("kg")

//            val orderid =databasefororder.push().key
            val orderid =System.currentTimeMillis()
            val rrr =databasefororder.child("${orderid}")

            itemlist.add(checkoutitem(kg!!,disc!!,price!!,imageurl!!))

            val data = hashMapOf(
                "name" to namer,
                "number" to number,
                "pincode" to pincode,
                "address" to address,
            )
//            val atul =""
//            val itemlist = hashMapOf(
//                "kg" to kg,
//                "discription" to disc,
//                "price" to price,
//                "image" to atul,
//
//            )

            dataa.setValue(data).addOnSuccessListener {
                rrr.child("order").setValue(itemlist).addOnSuccessListener {
                    databaseforordermanager.child("${orderid}").setValue(itemlist).addOnSuccessListener {
                        pro.visibility = View.GONE
                        Toast.makeText(this,"order placed", Toast.LENGTH_SHORT).show()
                        val intent = Intent(thsi@this,Mainpage::class.java)
                        startActivity(intent)
                    }
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

}