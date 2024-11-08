package code.fortomorrow.happyshopping.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.fortomorrow.happyshopping.model.Orders
import code.fortomorrow.happyshopping.view.ProductDetailsActivity
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.view.SearchProductsActivity
import com.bumptech.glide.Glide

class OrderAdapters(
    private val context: Context,
    private val messageList: ArrayList<Orders>,
    private val searchProductsActivity: SearchProductsActivity
) : RecyclerView.Adapter<OrderAdapters.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.products_items_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(messageList[position].image)
            .placeholder(R.drawable.product_sample_image).into(holder.product_image)
        holder.product_name.text = messageList[position].pname
        holder.product_price.text = messageList[position].price
        holder.view.setOnClickListener { v: View? ->
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("pid", messageList[position].pid)
            context.startActivity(intent)
            searchProductsActivity.finish()
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        internal val product_image: ImageView = itemView.findViewById(R.id.product_image)
        val product_name: TextView = itemView.findViewById(R.id.product_name)
        val product_price: TextView = itemView.findViewById(R.id.product_price)
    }
}
