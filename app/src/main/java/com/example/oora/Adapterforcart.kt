package com.example.oora

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private lateinit var name:String

class Adapterforcart(private val cartlist: MutableList<cartitem>,private val onTotalUpdated: (Double) -> Unit): RecyclerView.Adapter<aViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): aViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.productcart, parent, false)
        return aViewholder(view)

    }


    override fun onBindViewHolder(holder: aViewholder, position: Int) {
        //get the all the item of the cartlist
        val item = cartlist[position]

        holder.discription.text = item.discription
        holder.price.text = "${item.price}/kg"
        Glide.with(holder.itemView.context).load(item.imageurl).into(holder.image)
        holder.kgs.text =item.kg
        val itemTotal = item.price.toDouble() * item.kg.toDoubleOrNull()!! ?: 0.0
        holder.total.text = itemTotal.toString()
        updateGrandTotal()


        holder.deletebutton.setOnClickListener {

            val firestoredatabase = FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app").reference
            val currentuser = FirebaseAuth.getInstance().currentUser?.uid

            if (currentuser != null) {
                firestoredatabase.child("cart").child(currentuser).child(item.discription).removeValue().addOnSuccessListener {
                    // Remove item from RecyclerView list
                    if (position < cartlist.size) {
                        cartlist.removeAt(position)
//                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position,cartlist.size)
                    } else {
                        Toast.makeText(holder.itemView.context, "Error: Invalid item position", Toast.LENGTH_SHORT).show()
                    }
                }
                    .addOnFailureListener { e ->
                        Toast.makeText(holder.itemView.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }




}

    private fun updateGrandTotal() {
        var grandTotal = 0.0
        for (item in cartlist) {
            val itemTotal = item.price.toDouble() * item.kg.toDoubleOrNull()!! ?: 0.0
            grandTotal += itemTotal
        }
        onTotalUpdated(grandTotal) // Pass the grand total to the activity
    }


    override fun getItemCount(): Int {
        val a= cartlist.size
        return  a
    }



}



class aViewholder(private val itemview: View):RecyclerView.ViewHolder(itemview) {
    val image = itemview.findViewById<ImageView>(R.id.imageView5)
    val discription = itemview.findViewById<TextView>(R.id.textView5)
    val price =itemview.findViewById<TextView>(R.id.textView6)
    val kgs =itemview.findViewById<TextView>(R.id.editTextNumber5)
    val total= itemview.findViewById<TextView>(R.id.textView7)
    val deletebutton =itemview.findViewById<ImageView>(R.id.imageView7)


}

data class cartitem(
    val imageurl:String ="",
    val discription:String="",
    val price:String="" ,
    val kg:String =""
)

