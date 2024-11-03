package code.fortomorrow.happyshopping.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.fortomorrow.happyshopping.R
import code.fortomorrow.happyshopping.interfaces.ItemClickListener

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    @JvmField
    var txtProductName: TextView = itemView.findViewById(R.id.product_name)

    //txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
    @JvmField
    var txtProductPrice: TextView = itemView.findViewById(R.id.product_price)

    //public TextView txtProductDescription;
    @JvmField
    var imageView: ImageView = itemView.findViewById(R.id.product_image)
    private var listner: ItemClickListener? = null

    fun setItemClickListner(listner: ItemClickListener?) {
        this.listner = listner
    }

    override fun onClick(view: View) {
        listner!!.onClick(view, adapterPosition, false)
    }
}