package code.fortomorrow.riceapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import code.fortomorrow.riceapp.Interface.ItemClickListner;
import code.fortomorrow.riceapp.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,  txtProductPrice;
    //public TextView txtProductDescription;
    public ImageView imageView;
    public ItemClickListner listner;


    public ProductViewHolder(View itemView)
    {
        super(itemView);
        imageView =  itemView.findViewById(R.id.product_image);
        txtProductName =  itemView.findViewById(R.id.product_name);
        //txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice =  itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}