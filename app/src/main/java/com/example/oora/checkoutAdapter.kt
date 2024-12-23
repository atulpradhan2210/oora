package com.example.oora

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class checkoutAdapter(private val cartbuy:List<checkoutitem>): RecyclerView.Adapter<cViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forcheckoutcart, parent, false)
        return cViewholder(view)
    }

    override fun getItemCount(): Int {
        val a =cartbuy.size
        return a
    }

    override fun onBindViewHolder(holder: cViewholder, position: Int) {
        val item = cartbuy[position]
        holder.disc.text = "${item.discription}"
        holder.price.text = "${item.price}"
        holder.kgs.text = "${item.kg}"
        holder.total.text = "${item.price.toInt() * item.kg.toInt()}"
    }
}





class cViewholder(private val itemview: View):RecyclerView.ViewHolder(itemview) {
    val disc:TextView =itemview.findViewById(R.id.textView9)
    val price:TextView =itemview.findViewById(R.id.textView10)
    val kgs:TextView =itemview.findViewById(R.id.textView11)
    val total:TextView =itemview.findViewById(R.id.textView12)


}
data class checkoutitem (
    val kg:String="",
    val discription:String="",
    val price:String ="" ,
    val image:String ="",
)