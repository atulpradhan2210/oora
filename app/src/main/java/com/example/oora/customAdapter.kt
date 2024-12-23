package com.example.oora

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase



class customAdapter(private val vegfeed:List<ItemViewmodel>):
    RecyclerView.Adapter<Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val feedinflater =LayoutInflater.from(parent.context).inflate(R.layout.feed,parent,false)
        return Viewholder(feedinflater)
    }

    override fun getItemCount(): Int {
        val a = vegfeed.size
        return  a
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        // Get the current item from the vegfeed list
        val item = vegfeed[position]

        // Bind the data to the ViewHolder
        holder.nametext.text = "${item.discription}"
        holder.price.text = "${item.price}/kg"

        // Load image using Glide or any other image loading library
        // Example using Glide:
        Glide.with(holder.itemView.context)
            .load(item.imageurl)
            .into(holder.imageview)




        holder.add_to_cart.setOnClickListener {
            if (holder.kgs.text.toString().isEmpty()) {
                Toast.makeText(
                    holder.itemView.context,
                    "Please enter a value for kg",
                    Toast.LENGTH_SHORT
                ).show()
            } else {


                val firestoredatabase =
                    FirebaseDatabase.getInstance("https://oora-f2972-default-rtdb.asia-southeast1.firebasedatabase.app").reference
                val currentuser = FirebaseAuth.getInstance().currentUser?.uid
                val kg = holder.kgs.text.toString()
                holder.progress.visibility = View.VISIBLE


                val productdata = hashMapOf(
                    "imageurl" to item.imageurl,
                    "discription" to item.discription,
                    "price" to item.price,
                    "kg" to kg,
                )

                firestoredatabase.child("cart").child("${currentuser}").child("${item.discription}")
                    .setValue(productdata).addOnSuccessListener {
                       holder.progress.visibility = View.GONE

                }.addOnFailureListener { e ->

                }


            }

        }

        holder.directbuy.setOnClickListener {
            if (holder.kgs.text.toString().isEmpty()){
                Toast.makeText(holder.itemView.context,"Please enter a value for kg",Toast.LENGTH_SHORT).show()
            }else{
                val intent =Intent(holder.itemView.context,Directbuy::class.java)
                intent.putExtra("discription",item.discription)
                intent.putExtra("price",item.price)
                intent.putExtra("imageurl",item.imageurl)
                intent.putExtra("kg",holder.kgs.text.toString())
                holder.itemView.context.startActivity(intent)
            }
        }

    }

    }





class Viewholder(private val itemview:View):RecyclerView.ViewHolder(itemview) {
    var imageview: ImageView =itemview.findViewById(R.id.imageView6)
    val add_to_cart:Button =itemview.findViewById(R.id.button5)
    val nametext:TextView =itemview.findViewById(R.id.textView3)
    val price:TextView=itemview.findViewById(R.id.textView4)
    val kgs =itemview.findViewById<TextView>(R.id.editTextText3)
    val progress= itemview.findViewById<ProgressBar>(R.id.progressBar2)
    val directbuy:Button =itemview.findViewById(R.id.button7)

}




data class ItemViewmodel (
    val imageurl:String ="",
    val discription:String="",
    val price:String="" ,

)