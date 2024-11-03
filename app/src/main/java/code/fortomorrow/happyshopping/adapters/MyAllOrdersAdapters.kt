package code.fortomorrow.happyshopping.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.fortomorrow.happyshopping.model.AdminViewOrders
import code.fortomorrow.happyshopping.MyOrdersActivity
import code.fortomorrow.happyshopping.R

class MyAllOrdersAdapters(myOrdersActivity: MyOrdersActivity, ordersList: List<AdminViewOrders>) :
    RecyclerView.Adapter<MyAllOrdersAdapters.ViewHolder>() {
    private val context: Context = myOrdersActivity
    private val adminViewOrdersList = ordersList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.singlemyorders, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.product_name.text = "Product Name: " + adminViewOrdersList[position].pname
        holder.product_price.text = "Product price: " + adminViewOrdersList[position].price
        holder.product_date.visibility = View.VISIBLE
        holder.product_date.text = "Date: " + adminViewOrdersList[position].date
    }

    override fun getItemCount(): Int {
        return adminViewOrdersList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val product_name: TextView = itemView.findViewById(R.id.product_name)
        val product_price: TextView = itemView.findViewById(R.id.product_price)
        val product_date: TextView = itemView.findViewById(R.id.product_date)
    }
}
