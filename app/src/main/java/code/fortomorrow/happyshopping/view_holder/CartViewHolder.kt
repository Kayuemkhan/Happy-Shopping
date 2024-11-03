package code.fortomorrow.happyshopping.view_holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.interfaces.ItemClickListener

class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    @JvmField
    var txtProductName: TextView = itemView.findViewById(R.id.cart_product_name)
    @JvmField
    var txtProductPrice: TextView = itemView.findViewById(R.id.cart_product_price)
    @JvmField
    var txtProductQuantity: TextView = itemView.findViewById(R.id.cart_product_quantity)
    private var itemClickListener: ItemClickListener? = null

    override fun onClick(v: View) {
        itemClickListener!!.onClick(v, adapterPosition, false)
    }

    fun setItemClickListner(itemClickListener: ItemClickListener?) {
        this.itemClickListener = itemClickListener
    }
}
